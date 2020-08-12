package com.hfad.ideasworld.repository

import com.hfad.ideasworld.database.DataBaseEarthQuake
import com.hfad.ideasworld.database.EarthQuakeDao
import com.hfad.ideasworld.network.EarthQuakeService
import com.hfad.ideasworld.network.asDataBaseModel
import com.hfad.ideasworld.toListOfDatePairs
import com.hfad.ideasworld.toListPerRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EarthQuakesRepository @Inject constructor(private val earthQuakeDao: EarthQuakeDao, private val retrofitApi: EarthQuakeService){
   fun earthQuakes():List<DataBaseEarthQuake> =earthQuakeDao.getAllEarthQuakes()
   fun getEarthQuakesByMMI(mmi:Int):List<DataBaseEarthQuake> = earthQuakeDao.getEarthQuakesByMMI(mmi)

    fun getRangeStatistics(type: String)=earthQuakeDao.getStatsRangeByType(type)
    fun getYearDateStatistics()=earthQuakeDao.getYearDateStats()

    suspend fun refreshEarthQuakes(){
        withContext(Dispatchers.IO){
            val quakesList=retrofitApi.getEarthQuakes().await()
            earthQuakeDao.insertAll(quakesList.asDataBaseModel())
        }
    }

    suspend fun refresh() {
        withContext(Dispatchers.IO)
        {
            var list= retrofitApi.getEarthQuakes("8").await().asDataBaseModel()//EarthQuakeNetwork..getEarthquakesAsync("8").await().toEarthquakeList()
            earthQuakeDao.insertAll(list)
            list= retrofitApi.getEarthQuakes("7").await().asDataBaseModel()
            earthQuakeDao.insertAll(list)
            list= retrofitApi.getEarthQuakes("6").await().asDataBaseModel()
            earthQuakeDao.insertAll(list)
            list= retrofitApi.getEarthQuakes("5").await().asDataBaseModel()
            earthQuakeDao.insertAll(list)
            list= retrofitApi.getEarthQuakes("4").await().asDataBaseModel()
            earthQuakeDao.insertAll(list)
            list= retrofitApi.getEarthQuakes("3").await().asDataBaseModel()
            earthQuakeDao.insertAll(list)
        }
    }
    suspend fun rerfershStats(){
        withContext(Dispatchers.IO){
            earthQuakeDao.deleteRangeStats()
            earthQuakeDao.deleteYearDateStats()
            val refreshedStats=retrofitApi.getStatsAsync().await()
            earthQuakeDao.insertStats(refreshedStats.magnitudeCount.days7.toListPerRange("Week"))
            earthQuakeDao.insertStats(refreshedStats.magnitudeCount.days28.toListPerRange("Month"))
            earthQuakeDao.insertStats(refreshedStats.magnitudeCount.days365.toListPerRange("Year"))
            earthQuakeDao.insertYearDate(refreshedStats.rate.perDay.toListOfDatePairs())
        }
    }
}




