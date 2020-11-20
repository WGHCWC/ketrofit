package com.example.kotlinnet

import retrofit2.Call
import retrofit2.http.GET

/**
 * author：wghcwc
 * DATE: 20-11-12 15:50
 */
interface Service {
    @GET("test")
    fun test(): Call<Boolean>

    @GET("test2")
    fun test2(): Call<GeneralResponse<String>>

}