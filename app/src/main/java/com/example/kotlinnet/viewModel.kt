package com.example.kotlinnet

import androidx.lifecycle.*
import com.wghcwc.ketrofit.net.setApi
import retrofit2.Call

/**
 * author：wghcwc
 * DATE: 20-11-20 10:44
 */


fun <T> ViewModel.genApi(block: () -> Call<GeneralResponse<T>>): GenApiImpl<T> {
    return setApi(GenApiImpl(block())) as GenApiImpl<T>
}

fun <T> ViewModel.genApi(call: Call<GeneralResponse<T>>): GenApiImpl<T> {
    return setApi(GenApiImpl(call)) as GenApiImpl<T>
}
