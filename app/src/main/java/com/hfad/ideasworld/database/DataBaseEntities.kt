package com.hfad.ideasworld.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "earth_quake_table")
data class DataBaseEarthQuake constructor(
    @PrimaryKey
    val publicID: String,
    val depth: Double,
    val locality: String,
    val magnitude: Double,
    val mmi: Int,
    val quality: String,
    val time: String
)

@Entity(tableName = "per_date_table")
data class PerDate(
    @PrimaryKey
    val date:String,
    val count:Int
)

@Entity(tableName = "per_range_table",primaryKeys = ["type","mmi"] )
data class PerRange(
    val type:String,
    val mmi:Int,
    val count:Int
)