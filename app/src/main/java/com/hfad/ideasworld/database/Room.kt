package com.hfad.ideasworld.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hfad.ideasworld.network.Feature
import com.hfad.ideasworld.network.statistic.Statistic

@Dao
interface EarthQuakeDao{
    @Query("select * from earth_quake_table order by time desc")
    fun getAllEarthQuakes(): List<DataBaseEarthQuake>
    @Query("select * from earth_quake_table where :mmi<=mmi order by time desc")
    fun getEarthQuakesByMMI(mmi:Int):List<DataBaseEarthQuake>
    @Query("select * from earth_quake_table where publicID=:publicID")
    fun getEarthQuakeByID(publicID:String):DataBaseEarthQuake
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( feature: List<DataBaseEarthQuake>)
    @Query("select * from per_range_table where type=:type")
    fun getStatsRangeByType(type:String):List<PerRange>
    @Query("delete from per_range_table")
    fun deleteRangeStats()
    @Insert(onConflict =OnConflictStrategy.REPLACE)
    fun insertStats(list:List<PerRange>)
    @Query("select * from per_date_table")
    fun getYearDateStats():List<PerDate>
    @Query("delete from per_date_table")
    fun deleteYearDateStats()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertYearDate(list: List<PerDate>)
}

@Database(entities = [DataBaseEarthQuake::class,PerRange::class,PerDate::class],version = 2,exportSchema = false)
abstract class EarthQuakeBase:RoomDatabase(){
abstract val earthQuakeDao:EarthQuakeDao
}

private lateinit var INSTANCE:EarthQuakeBase

fun getDataBase(context: Context):EarthQuakeBase{
    synchronized(EarthQuakeBase::class.java){
        if (!::INSTANCE.isInitialized){
                INSTANCE=Room.databaseBuilder(context.applicationContext,
                    EarthQuakeBase::class.java,
                    "earthQuakes").fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}

