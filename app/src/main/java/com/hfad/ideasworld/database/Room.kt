package com.hfad.ideasworld.database

import android.content.Context
import androidx.room.*
import com.hfad.ideasworld.App
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

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
abstract fun earthQuakeDao():EarthQuakeDao
}

@Module
@InstallIn(ApplicationComponent::class)
object DataBaseModule{

    @Singleton
    @Provides
    fun provideEarthQuakeBase(@ApplicationContext context: Context)= Room.databaseBuilder(context,EarthQuakeBase::class.java,"earthQuakes").build()

    @Singleton
    @Provides
    fun provideEarthQuakesDao(dataBaseEarthQuake: EarthQuakeBase):EarthQuakeDao=dataBaseEarthQuake.earthQuakeDao()

}

