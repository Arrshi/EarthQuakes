package com.hfad.ideasworld.ui.stats

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.hfad.ideasworld.R
import com.hfad.ideasworld.databinding.FragmentNotificationsBinding
import java.text.SimpleDateFormat

class NotificationsFragment : Fragment() {

    private val notificationsViewModel: NotificationsViewModel by lazy {
        val activity= requireNotNull(this.activity){
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this,NotificationViewModelFactory(activity.application)).get(NotificationsViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding=DataBindingUtil.inflate<FragmentNotificationsBinding>(inflater,R.layout.fragment_notifications,container,false)
        binding.apply {
            viewModel=notificationsViewModel
            lifecycleOwner=viewLifecycleOwner
            horizotalBar.description.text=""
            horizotalBar.isHighlightPerTapEnabled=false
            horizotalBar.isHighlightPerDragEnabled=false
            horizotalBar.xAxis.valueFormatter=object : ValueFormatter(){
                val sdf=SimpleDateFormat("dd.MM.yyyy")
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return sdf.format(value.toLong())
                }
            }
            pieChart.description.text=""
            pieChart.setDrawEntryLabels(false)
            pieChart.legend.isWordWrapEnabled=true
            pieChart.minAngleForSlices=12f
        }
        binding.swipeLayout.setOnRefreshListener { notificationsViewModel.onSwipe() }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.stats_type_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.year_detailed_type->notificationsViewModel.changeCurrentType(ShowStatsType.YEAR_DETAILED)
            R.id.year_type->notificationsViewModel.changeCurrentType(ShowStatsType.YEAR)
            R.id.mount_type->notificationsViewModel.changeCurrentType(ShowStatsType.MONTH)
            R.id.week_type->notificationsViewModel.changeCurrentType(ShowStatsType.WEEK)
        }
        return super.onOptionsItemSelected(item)
    }
}