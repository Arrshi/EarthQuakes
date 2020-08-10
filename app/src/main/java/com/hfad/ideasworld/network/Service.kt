package com.hfad.ideasworld.network

import com.hfad.ideasworld.network.statistic.Statistic
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private val BASE_URL="https://api.geonet.org.nz/"

interface EarthQuakeService {
    @GET("quake")
    fun getEarthQuakes(@Query(value = "MMI") magnitude:String="0"): Deferred<EarthQuakeJSON>

    @GET(value = "quake/{publicID}")
    fun getByPublicIDAsync(@Path("publicID")publicID:String ):Deferred<EarthQuakeJSON>

    @GET("quake/stats")
    fun getStatsAsync():Deferred<Statistic>

}

object EarthQuakeNetwork {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


    val retrofitApi = retrofit.create(EarthQuakeService::class.java)

}