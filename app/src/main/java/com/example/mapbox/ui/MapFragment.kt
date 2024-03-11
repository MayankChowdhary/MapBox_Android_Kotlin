package com.example.mapbox.ui


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
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
import com.mapbox.mapboxsdk.maps.MapView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var viewState: MainViewState
    private val viewModel: MainViewModel by viewModels()
    var handler: Handler = Handler(Looper.getMainLooper())
    var doubleBackToExitPressedOnce = false
    private lateinit var mapView: MapView


    private var styleUrl = Constants.MapType.barikoiLiberty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(requireActivity());
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
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
                Vibration.vibrate(100,"Map Style Changed")
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
                    }else -> {
                        styleUrl = Constants.MapType.default
                        mapView.getMapAsync { map -> map.setStyle(styleUrl) }
                        true
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)





        mapView = binding.mapView
        mapView.getMapAsync { map ->
            map.setStyle(styleUrl)
            map.cameraPosition = CameraPosition.Builder().target(LatLng(0.0, 0.0)).zoom(1.0).build()
        }



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
}