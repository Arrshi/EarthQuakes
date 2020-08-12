package com.hfad.ideasworld.ui.earthquake

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.ideasworld.R
import com.hfad.ideasworld.databinding.FragmentHomeBinding
import com.hfad.ideasworld.ui.earthquake.sorting.SortingDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    private val viewAdapter=EarthQuakeAdapter(OnClickListenerEarthQuake {

       findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToDetailsFragment2(it))
    })

    private val earthQuakeItemDecorator=EarthQuakeItemDecorator(10)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
       val binding:FragmentHomeBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        binding.apply {
            viewModel=homeViewModel
            quakeView.apply {
                layoutManager=LinearLayoutManager(context)
                adapter=viewAdapter
                addItemDecoration(earthQuakeItemDecorator)
            }
        }
        binding.refreshSwipe.setOnRefreshListener {
            refresh_swipe.isRefreshing=true
            homeViewModel.onRefresh()
            refresh_swipe.isRefreshing=false
        }
        homeViewModel._quakesList.observe(viewLifecycleOwner, Observer {
           viewAdapter.submitList(it)
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sorting_earthquake,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val dialogFragment=SortingDialogFragment()
        dialogFragment.setTargetFragment(this,1)
        dialogFragment.show(parentFragmentManager.beginTransaction(),dialogFragment.javaClass.name)
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            val resultType= data?.extras?.getInt(SortingDialogFragment.MESSAGE_CONFRIM)
            homeViewModel.currentType=resultType!!
        }
    }
}