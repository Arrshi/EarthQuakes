package com.hfad.ideasworld.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.hfad.ideasworld.database.DataBaseEarthQuake
import com.hfad.ideasworld.database.EarthQuakeBase
import com.hfad.ideasworld.database.PerRange
import com.hfad.ideasworld.network.EarthQuakeNetwork
import com.hfad.ideasworld.network.asDataBaseModel
import com.hfad.ideasworld.toListOfDatePairs
import com.hfad.ideasworld.toListPerRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EarthQuakesRepository(val dataBaseEarthQuake: EarthQuakeBase){
   fun earthQuakes():List<DataBaseEarthQuake> =dataBaseEarthQuake.earthQuakeDao.getAllEarthQuakes()

   fun getEarthQuakesByMMI(mmi:Int):List<DataBaseEarthQuake>{
        return dataBaseEarthQuake.earthQuakeDao.getEarthQuakesByMMI(mmi)
    }

    fun getRangeStatistics(type: String)=dataBaseEarthQuake.earthQuakeDao.getStatsRangeByType(type)
    fun getYearDateStatistics()=dataBaseEarthQuake.earthQuakeDao.getYearDateStats()

    suspend fun refreshEarthQuakes(){
        withContext(Dispatchers.IO){
            val quakesList=EarthQuakeNetwork.retrofitApi.getEarthQuakes().await()
            dataBaseEarthQuake.earthQuakeDao.insertAll(quakesList.asDataBaseModel())
        }
    }

    suspend fun refresh() {
        withContext(Dispatchers.IO)
        {
            var list= EarthQuakeNetwork.retrofitApi.getEarthQuakes("8").await().asDataBaseModel()//EarthQuakeNetwork..getEarthquakesAsync("8").await().toEarthquakeList()
            dataBaseEarthQuake.earthQuakeDao.insertAll(list)
            list= EarthQuakeNetwork.retrofitApi.getEarthQuakes("7").await().asDataBaseModel()
            dataBaseEarthQuake.earthQuakeDao.insertAll(list)
            list= EarthQuakeNetwork.retrofitApi.getEarthQuakes("6").await().asDataBaseModel()
            dataBaseEarthQuake.earthQuakeDao.insertAll(list)
            list= EarthQuakeNetwork.retrofitApi.getEarthQuakes("5").await().asDataBaseModel()
            dataBaseEarthQuake.earthQuakeDao.insertAll(list)
            list= EarthQuakeNetwork.retrofitApi.getEarthQuakes("4").await().asDataBaseModel()
            dataBaseEarthQuake.earthQuakeDao.insertAll(list)
            list= EarthQuakeNetwork.retrofitApi.getEarthQuakes("3").await().asDataBaseModel()
            dataBaseEarthQuake.earthQuakeDao.insertAll(list)
        }
    }
    suspend fun rerfershStats(){
        withContext(Dispatchers.IO){
            dataBaseEarthQuake.earthQuakeDao.deleteRangeStats()
            dataBaseEarthQuake.earthQuakeDao.deleteYearDateStats()
            val refreshedStats=EarthQuakeNetwork.retrofitApi.getStatsAsync().await()
            dataBaseEarthQuake.earthQuakeDao.insertStats(refreshedStats.magnitudeCount.days7.toListPerRange("Week"))
            dataBaseEarthQuake.earthQuakeDao.insertStats(refreshedStats.magnitudeCount.days28.toListPerRange("Month"))
            dataBaseEarthQuake.earthQuakeDao.insertStats(refreshedStats.magnitudeCount.days365.toListPerRange("Year"))
            dataBaseEarthQuake.earthQuakeDao.insertYearDate(refreshedStats.rate.perDay.toListOfDatePairs())
        }
    }
}




