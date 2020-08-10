package com.hfad.ideasworld.ui.stats

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import com.hfad.ideasworld.database.getDataBase
import com.hfad.ideasworld.repository.EarthQuakesRepository
import com.hfad.ideasworld.toHorizontalBarEntry
import com.hfad.ideasworld.toPieEntry
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.IllegalArgumentException

class NotificationsViewModel(val app:Application) : ViewModel() {
    private val viewModelJob= Job()
    private val viewModelScope= CoroutineScope(viewModelJob+Dispatchers.Main)

    private val earthQuakesRepository=EarthQuakesRepository(getDataBase(context = app))
    private val currentType=MutableLiveData(ShowStatsType.YEAR_DETAILED)

    private val _isRefreshing=MutableLiveData(false)
    val isRefreshing:LiveData<Boolean>
        get() = _isRefreshing

    private val _isHorizontalBar=MutableLiveData(true)
    val isHorizontalBar:LiveData<Boolean>
        get() = _isHorizontalBar

    private val _horizontalBarData=MutableLiveData<List<BarEntry>>()
    val horizontalBarData:LiveData<List<BarEntry>>
        get() = _horizontalBarData


    private val _pieData=MutableLiveData<List<PieEntry>>()
    val pieData:LiveData<List<PieEntry>>
        get() = _pieData

    init {
        loadWithConnection()
    }
    private fun loadDataByType(){
        viewModelScope.launch {
            if (currentType.value==ShowStatsType.YEAR_DETAILED){
                _horizontalBarData.value= withContext(Dispatchers.IO){earthQuakesRepository.getYearDateStatistics().toHorizontalBarEntry()}
            }
            else{

                    _pieData.value= withContext(Dispatchers.IO){earthQuakesRepository.getRangeStatistics(when(currentType.value){
                        ShowStatsType.WEEK -> ShowStatsType.WEEK.type
                        ShowStatsType.MONTH ->ShowStatsType.MONTH.type
                        ShowStatsType.YEAR -> ShowStatsType.YEAR.type
                        else -> throw IllegalArgumentException("Unknown type")
                    }).toPieEntry()}
                }
            }
        }

    private fun loadWithConnection(){
        viewModelScope.launch {
            if (currentType.value==ShowStatsType.YEAR_DETAILED){
                _horizontalBarData.value= withContext(Dispatchers.IO){earthQuakesRepository.getYearDateStatistics().toHorizontalBarEntry()}
                if(_horizontalBarData.value.isNullOrEmpty())
                    refreshDataFromRepository()
            }
            else{
                _pieData.value= withContext(Dispatchers.IO){earthQuakesRepository.getRangeStatistics(when(currentType.value){
                    ShowStatsType.WEEK -> ShowStatsType.WEEK.type
                    ShowStatsType.MONTH ->ShowStatsType.MONTH.type
                    ShowStatsType.YEAR -> ShowStatsType.YEAR.type
                    else -> throw IllegalArgumentException("Unknown type")
                }).toPieEntry()}
                if (_pieData.value.isNullOrEmpty())
                    refreshDataFromRepository()
            }
        }
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            _isRefreshing.value=true
            try {
                earthQuakesRepository.rerfershStats()
            } catch (e: IOException) {
                Toast.makeText(app,e.message,Toast.LENGTH_SHORT).show()
            }
            loadDataByType()
            _isRefreshing.value=false
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun changeCurrentType(type:ShowStatsType){
        if(currentType.value!=type){
            currentType.value=type
            _isHorizontalBar.value=currentType.value==ShowStatsType.YEAR_DETAILED
            loadDataByType()
        }
    }

    fun onSwipe(){
        refreshDataFromRepository()
    }
}

enum class ShowStatsType(val type:String){
    YEAR_DETAILED("Details"),
    WEEK("Week")
    ,MONTH("Month")
    ,YEAR("Year");


}