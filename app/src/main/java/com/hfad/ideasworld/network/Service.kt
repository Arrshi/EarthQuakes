package com.hfad.ideasworld.network

import com.hfad.ideasworld.network.statistic.Statistic
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton


private const val BASE_URL="https://api.geonet.org.nz/"

interface EarthQuakeService {
    @GET("quake")
    fun getEarthQuakes(@Query(value = "MMI") magnitude:String="0"): Deferred<EarthQuakeJSON>

    @GET(value = "quake/{publicID}")
    fun getByPublicIDAsync(@Path("publicID")publicID:String ):Deferred<EarthQuakeJSON>

    @GET("quake/stats")
    fun getStatsAsync():Deferred<Statistic>

}


@Module
@InstallIn(ApplicationComponent::class)
object EarthQuakeNetworkModule {
    @Singleton
    @Provides
    fun provideMoshiInstance():Moshi=Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(moshi:Moshi):Retrofit=Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create(moshi))
                                                                        .addCallAdapterFactory(CoroutineCallAdapterFactory()).build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit):EarthQuakeService=retrofit.create(EarthQuakeService::class.java)

}
