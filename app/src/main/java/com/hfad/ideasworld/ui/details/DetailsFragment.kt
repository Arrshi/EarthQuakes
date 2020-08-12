package com.hfad.ideasworld.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hfad.ideasworld.R
import com.hfad.ideasworld.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private val detailsViewModel:DetailsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=DataBindingUtil.inflate<FragmentDetailsBinding>(inflater,R.layout.fragment_details,container,false)
        detailsViewModel.publicID=DetailsFragmentArgs.fromBundle(requireArguments()).publicID
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
