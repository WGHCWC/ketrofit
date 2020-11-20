package com.example.kotlinnet

import androidx.lifecycle.*
import com.wghcwc.ketrofit.net.setApi
import retrofit2.Call

/**
 * author：wghcwc
 * DATE: 20-11-14 18:12
 */


fun <T> LifecycleOwner.genApi(block: () -> Call<GeneralResponse<T>>): GenApiImpl<T> {
    return setApi(GenApiImpl(block())) as GenApiImpl<T>
}

fun <T> LifecycleOwner.genApi(call: Call<GeneralResponse<T>>): GenApiImpl<T> {
    return setApi(GenApiImpl(call)) as GenApiImpl<T>
}


