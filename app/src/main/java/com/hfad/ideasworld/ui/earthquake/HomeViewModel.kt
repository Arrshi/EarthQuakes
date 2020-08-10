package com.hfad.ideasworld.ui.earthquake

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.internal.Objects
import com.hfad.ideasworld.database.DataBaseEarthQuake
import com.hfad.ideasworld.database.getDataBase
import com.hfad.ideasworld.repository.EarthQuakesRepository
import kotlinx.coroutines.*
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob= Job()

    private val viewModelScope= CoroutineScope(viewModelJob+Dispatchers.Main)

    private var _eventNetworkError = MutableLiveData(false)

    private val _tryToRefresh=MutableLiveData<Boolean>(false)
    val tryToRefresh:LiveData<Boolean>
        get() = _tryToRefresh

    var currentType:Int=-1
        set(value) {
            if (value!=field){
                field = value
                refreshDataFromRepositoryByMMI()
            }
        }

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    var _quakesList=MutableLiveData<List<DataBaseEarthQuake>>()
    private val earthQuakesRepository=EarthQuakesRepository(getDataBase(context = application))
    init {
        viewModelScope.launch {
        _quakesList.value= withContext(Dispatchers.IO){earthQuakesRepository.earthQuakes()}
            if(_quakesList.value.isNullOrEmpty()) {
                refreshAllEarthQuakes()
            }
        }

    }

    private fun refreshAllEarthQuakes(){
        viewModelScope.launch {
            _tryToRefresh.value=true
            try
            {
                earthQuakesRepository.refreshEarthQuakes()
                _quakesList.value= withContext(Dispatchers.IO){ earthQuakesRepository.getEarthQuakesByMMI(currentType)}
                earthQuakesRepository.refresh()
                _eventNetworkError.value=false
                _isNetworkErrorShown.value=false
            }
            catch (e:Exception)
            {
                _eventNetworkError.value=true
            }
            _quakesList.value= withContext(Dispatchers.IO){ earthQuakesRepository.getEarthQuakesByMMI(currentType)}
            _tryToRefresh.value=false
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

   private fun refreshDataFromRepositoryByMMI(){
       viewModelScope.launch {
              try {
                  _quakesList.value=withContext(Dispatchers.IO){ earthQuakesRepository.getEarthQuakesByMMI(currentType)}

              }catch (e:Exception){
                  Toast.makeText(getApplication(),e.message,Toast.LENGTH_SHORT).show()
              }
       }
   }

    fun onRefresh(){
        refreshAllEarthQuakes()
    }

}