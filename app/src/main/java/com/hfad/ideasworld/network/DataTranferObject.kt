package com.hfad.ideasworld.network

import com.hfad.ideasworld.database.DataBaseEarthQuake
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EarthQuakeJSON(
    val features: List<Feature>,
    val type: String
)

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)

data class Geometry(
    val coordinates: List<Double>,
    val type: String
)

data class Properties(
    val depth: Double,
    val locality: String,
    val magnitude: Double,
    val mmi: Int,
    val publicID: String,
    val quality: String,
    val time: String
)


fun EarthQuakeJSON.asDataBaseModel():List<DataBaseEarthQuake>{
    return features.map {
        DataBaseEarthQuake(
            publicID = it.properties.publicID,
            time =it.properties.time,
            depth = it.properties.depth,
            magnitude =it.properties.magnitude,
            locality = it.properties.locality,
            mmi = it.properties.mmi,
            quality = it.properties.quality
        )
    }
}