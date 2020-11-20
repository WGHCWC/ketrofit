package com.example.kotlinnet

import android.util.Log
import com.wghcwc.ketrofit.net.ApiContextImpl
import com.wghcwc.ketrofit.net.ApiErr
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * author：wghcwc
 * DATE: 20-11-16 15:58
 *
 */
class GenApiImpl<T>(private val call: Call<com.example.kotlinnet.GeneralResponse<T>>) : ApiContextImpl<T>() {

    override fun down(success: T.() -> Unit): Disposed {
        job = onScope.launch(exceptionHandler) {
            Log.d("TAG", "down: "+Thread.currentThread().name)
            call.enqueue(object : Callback<com.example.kotlinnet.GeneralResponse<T>> {
                override fun onResponse(
                    call: Call<com.example.kotlinnet.GeneralResponse<T>>,
                    response: Response<com.example.kotlinnet.GeneralResponse<T>>,
                ) {
                    downScope.launch(exceptionHandler) {
                        debug(response.raw())
                        val data = response.body()
                        when {
                            data?.model == null -> err(FlowExError(ApiErr.ServiceNoDataErr))
                            data.code != 200 -> err(FlowExError(ApiErr.ServiceErr).setData(data))
                            else -> success(data.model)
                        }
                        finishRequest(true)
                    }
                }

                override fun onFailure(call: Call<com.example.kotlinnet.GeneralResponse<T>>, e: Throwable) {
                    downScope.launch {
                        err(FlowExError(e))
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


    class FlowExError(throwable: Throwable) : ApiContextImpl.ApiError(throwable) {
        fun setData(resData: com.example.kotlinnet.GeneralResponse<*>): FlowExError {
            this.code = resData.code
            this.msg = resData.msg
            this.data = resData.model
            return this
        }
    }


}

