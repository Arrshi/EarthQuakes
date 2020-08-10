package com.hfad.ideasworld.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.hfad.ideasworld.network.EarthQuakeNetwork
import com.hfad.ideasworld.network.Feature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailsViewModel(publicID:String):ViewModel(){
    private val viewModelJob= Job()
    private val viewModelScope= CoroutineScope(viewModelJob+Dispatchers.Main)
    private lateinit var googleMap:GoogleMap
    private val api=EarthQuakeNetwork.retrofitApi
    private val _earthquake=MutableLiveData<Feature>()
    val earthquake:LiveData<Feature> get() = _earthquake

    private val _isNetworkError=MutableLiveData<Boolean>(false)
    val isNetworkError:LiveData<Boolean>
        get() = _isNetworkError


    init {
        viewModelScope.launch {
            try {
                val earthQuake=api.getByPublicIDAsync(publicID).await().features
                _earthquake.value=if (earthQuake.isNullOrEmpty()){
                    null
                }
                else{
                    earthQuake[0]
                }
                _earthquake.value?.let {
                    val tempLntLatLng=LatLng(it.geometry.coordinates[1],it.geometry.coordinates[0])
                    googleMap.addMarker(MarkerOptions().position(tempLntLatLng))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(tempLntLatLng))
                }
            }catch (e:Exception){
                _isNetworkError.value=true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onSetGoogleMap(map:GoogleMap){
        googleMap =map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds( LatLng(-48.0, 165.0), LatLng(-33.0, 180.0)),0))
    }

}

