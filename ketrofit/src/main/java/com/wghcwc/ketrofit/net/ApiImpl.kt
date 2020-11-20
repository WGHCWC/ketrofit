package com.wghcwc.ketrofit.net

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * author：wghcwc
 * DATE: 20-11-18 14:35
 *
 * 通用Call请求
 */

open class ApiImpl<T>(private val call: Call<T>) : ApiContextImpl<T>() {
    override fun down(success: T.() -> Unit): Disposed {
        job = onScope.launch(exceptionHandler) {
            call.enqueue(object : Callback<T> {
                override fun onResponse(
                    call: Call<T>,
                    response: Response<T>,
                ) {
                    downScope.launch(exceptionHandler) {
                        debug(response.raw())
                        val data = response.body()
                        if (data == null) {
                            err(ApiError(ApiErr.ServiceNoDataErr))
                        } else {
                            success(data)
                        }
                        finishRequest(true)
                    }
                }

                override fun onFailure(call: Call<T>, e: Throwable) {
                    downScope.launch {
                        err(ApiError(e))
                        finishRequest(false)
                    }
                }

            })

        }
        return disposed
    }


    override fun cancel() {
        super.cancel()
        if (call.isCanceled) call.cancel()
    }


}