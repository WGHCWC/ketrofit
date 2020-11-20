package com.wghcwc.ketrofit.net

import retrofit2.Call

/**
 * author：wghcwc
 * DATE: 20-11-16 16:06
 */

fun <T> Call<T>.api(block: ApiContextImpl<*>.() -> Unit): ApiImpl<T> {
    val flow = ApiImpl(this)
    flow.block()
    return flow
}
fun <T> Call<T>.err(block: ApiContextImpl.ApiError.() -> Unit): ApiImpl<T> {
    return ApiImpl(this).error(block) as ApiImpl<T>
}
infix fun <T> Call<T>.down(block: T.() -> Unit): ApiContextImpl<T>.Disposed {
    return ApiImpl(this).down(block)
}


