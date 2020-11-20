package com.example.kotlinnet

import com.wghcwc.ketrofit.net.ApiContextImpl
import retrofit2.Call

/**
 * author：wghcwc
 * DATE: 20-11-16 16:06
 */

fun <T> Call<GeneralResponse<T>>.api(block: ApiContextImpl<*>.() -> Unit = {}): GenApiImpl<T> {
    val flow = GenApiImpl(this)
    flow.block()
    return flow
}


fun <T> Call<GeneralResponse<T>>.err(block: ApiContextImpl.ApiError.() -> Unit): GenApiImpl<T> {
    return GenApiImpl(this).error(block) as GenApiImpl<T>
}




