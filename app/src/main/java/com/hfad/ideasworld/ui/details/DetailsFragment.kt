package com.hfad.ideasworld.ui.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.hfad.ideasworld.R
import com.hfad.ideasworld.databinding.FragmentDetailsBinding
import com.hfad.ideasworld.network.EarthQuakeNetwork
import com.hfad.ideasworld.ui.earthquake.HomeViewModel
import com.hfad.ideasworld.ui.earthquake.HomeViewModelFactory
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {
    private val detailsViewModel:DetailsViewModel by lazy {
        val tempPublicID=DetailsFragmentArgs.fromBundle(requireArguments()).publicID
        ViewModelProviders.of(this,DetailsViewModelFactory(tempPublicID)).get(DetailsViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=DataBindingUtil.inflate<FragmentDetailsBinding>(inflater,R.layout.fragment_details,container,false)
        binding.apply {
            viewModel=detailsViewModel
            lifecycleOwner=viewLifecycleOwner
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        googleMapView.onCreate(savedInstanceState)
        googleMapView.onResume()
        googleMapView.getMapAsync {
            it?.let {
                detailsViewModel.onSetGoogleMap(it)
            }
        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        googleMapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

}
