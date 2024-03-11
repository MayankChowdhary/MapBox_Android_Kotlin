package com.example.mapbox.ui


import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.mapbox.R
import com.example.mapbox.databinding.FragmentMapBinding
import com.example.mapbox.utils.Constants
import com.example.mapbox.utils.Vibration
import com.example.mapbox.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.engine.LocationEngineRequest
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.permissions.PermissionsListener
import com.mapbox.mapboxsdk.location.permissions.PermissionsManager
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapBinding
    private lateinit var viewState: MainViewState
    private val viewModel: MainViewModel by viewModels()
    var handler: Handler = Handler(Looper.getMainLooper())
    var doubleBackToExitPressedOnce = false
    private lateinit var mapView: MapView

    private var lastLocation: Location? = null
    private var permissionsManager: PermissionsManager? = null
    private var locationComponent: LocationComponent? = null
    private lateinit var maplibreMap: MapboxMap
    val SAVED_STATE_LOCATION: String = "SAVED_STATE_LOCATION"


    private var styleUrl = Constants.MapType.default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(requireActivity());
        if (savedInstanceState != null) {
            lastLocation = savedInstanceState.getParcelable(SAVED_STATE_LOCATION)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // Save the user's current game state
        savedInstanceState.putParcelable(SAVED_STATE_LOCATION, lastLocation);
        mapView.onSaveInstanceState(savedInstanceState)
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState)
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (doubleBackToExitPressedOnce) {
                requireActivity().finish();
            }
            doubleBackToExitPressedOnce = true;
            Snackbar.make(requireView(), "Please click BACK again to exit", Snackbar.LENGTH_SHORT)
                .show()
            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()

        viewState = MainActivity.viewState
        binding.viewState = viewState


        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                Vibration.vibrate(100, null)
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_default -> {
                        styleUrl = Constants.MapType.default
                        mapView.getMapAsync { map -> map.setStyle(styleUrl) }
                        true
                    }

                    R.id.action_dark -> {
                        styleUrl = Constants.MapType.barikoiDark
                        mapView.getMapAsync { map -> map.setStyle(styleUrl) }
                        true
                    }

                    R.id.action_liberty -> {
                        styleUrl = Constants.MapType.barikoiLiberty
                        mapView.getMapAsync { map -> map.setStyle(styleUrl) }
                        true
                    }

                    else -> {
                        styleUrl = Constants.MapType.default
                        mapView.getMapAsync { map -> map.setStyle(styleUrl) }
                        true
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.fabLocation.isEnabled = false
        binding.fabMarker.isEnabled = false

        binding.fabLocation.setOnClickListener {
            Vibration.vibrate(100, null)
            locationComponent?.lastKnownLocation?.let {
                maplibreMap.cameraPosition =
                    CameraPosition.Builder().target(LatLng(it.latitude, it.longitude)).zoom(
                        4.0
                    ).build()
            }
        }

        binding.fabMarker.setOnClickListener {
            Vibration.vibrate(100, null)
        }

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        checkPermissions()

        /*NavHostFragment.findNavController(this)
            .navigate(MapFragmentDirections.actionFirstFragmentToSecondFragment(it))*/
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }


    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mapboxMap: MapboxMap) {
        this.maplibreMap = mapboxMap
        maplibreMap.setStyle(styleUrl) { style: Style ->
            locationComponent = maplibreMap.locationComponent
            val locationComponentOptions =
                LocationComponentOptions.builder(requireActivity())
                    .pulseEnabled(true)
                    .pulseColor(Color.RED)             // Set color of pulse
                    .foregroundTintColor(Color.BLACK)  // Set color of user location
                    .build()

            val locationComponentActivationOptions =
                buildLocationComponentActivationOptions(style, locationComponentOptions)
            locationComponent!!.activateLocationComponent(locationComponentActivationOptions)
            locationComponent!!.isLocationComponentEnabled = true
            locationComponent!!.cameraMode = CameraMode.TRACKING
            locationComponent!!.forceLocationUpdate(lastLocation)
        }

        binding.fabLocation.isEnabled = true
        binding.fabMarker.isEnabled = true
    }

    private fun buildLocationComponentActivationOptions(
        style: Style,
        locationComponentOptions: LocationComponentOptions
    ): LocationComponentActivationOptions {
        return LocationComponentActivationOptions
            .builder(requireActivity(), style)
            .locationComponentOptions(locationComponentOptions)
            .useDefaultLocationEngine(true)
            .locationEngineRequest(
                LocationEngineRequest.Builder(750)
                    .setFastestInterval(750)
                    .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                    .build()
            )
            .build()
    }

    private fun checkPermissions() {
        if (PermissionsManager.areLocationPermissionsGranted(requireActivity())) {
            mapView.getMapAsync(this)
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                    Toast.makeText(
                        requireActivity(),
                        "You need to accept location permissions.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        mapView.getMapAsync(this@MapFragment)

                    } else {
                        requireActivity().finish()
                    }
                }
            })
            permissionsManager!!.requestLocationPermissions(requireActivity())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}