package com.hfad.ideasworld.ui.earthquake

import android.content.Context
import android.widget.Toast
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hfad.ideasworld.database.DataBaseEarthQuake
import com.hfad.ideasworld.repository.EarthQuakesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*

class HomeViewModel @ViewModelInject constructor(private val earthQuakesRepository: EarthQuakesRepository,
                                                 @Assisted private val savedInstance:SavedStateHandle,
                                                 @ApplicationContext private val context: Context) : ViewModel() {
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

    private var _isNetworkErrorShown = MutableLiveData(false)


    var _quakesList=MutableLiveData<List<DataBaseEarthQuake>>()
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
                  Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
              }
       }
   }

    fun onRefresh(){
        refreshAllEarthQuakes()
    }

}