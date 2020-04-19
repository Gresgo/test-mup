package com.test.testmup

import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    companion object {
        fun create(): Api {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(LOCALHOST)
                .build()

            return retrofit.create(Api::class.java)
        }
    }

    @POST("writeGeo/")
    fun writeToDb(
        @Body geo: LatLong
    ): Single<Response<String>>

}