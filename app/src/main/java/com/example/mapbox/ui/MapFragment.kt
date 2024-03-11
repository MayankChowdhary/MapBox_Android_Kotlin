package com.example.mapbox.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapbox.R
import com.example.mapbox.databinding.FragmentMapBinding
import com.example.mapbox.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var viewState: MainViewState
    private val viewModel: MainViewModel by viewModels()
    private lateinit var searchView: SearchView
    var handler: Handler = Handler(Looper.getMainLooper())
    var doubleBackToExitPressedOnce = false
    val adapterGroupie = GroupieAdapter()


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
        viewState = MainActivity.viewState
        binding.viewState = viewState
        initRecyclerView()


        viewModel.wikiClickLiveData.observe(viewLifecycleOwner) {
            it?.let {
                NavHostFragment.findNavController(this)
                    .navigate(MapFragmentDirections.actionFirstFragmentToSecondFragment(it))
                viewModel.wikiClickLiveData.value = null
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.hasFixedSize()
        recyclerView.adapter = adapterGroupie
    }
}