package com.wghcwc.ketrofit.net

import androidx.lifecycle.*
import retrofit2.Call

/**
 * author：wghcwc
 * DATE: 20-11-20 10:44
 */


fun <T> ViewModel.api(block: () -> Call<T>): ApiImpl<T> {
    return setApi(ApiImpl(block())) as ApiImpl<T>
}

fun <T> ViewModel.api(call: Call<T>): ApiImpl<T> {
    return setApi(ApiImpl(call)) as ApiImpl<T>
}

fun <T> ViewModel.setApi(flow: ApiContextImpl<T>): ApiContextImpl<T> {
    flow.downScope(viewModelScope)
    flow.onScope(viewModelScope)
    return flow
}

