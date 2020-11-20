package com.wghcwc.ketrofit.net

import kotlinx.coroutines.*

/**
 * author：wghcwc
 * DATE: 20-11-18 14:35
 *
 * 协程环境
 */

abstract class ApiContextImpl<V> {

    /**
     *请求任务
     * */
    protected var job: Job? = null

    /**
     * err block
     * */
    protected var err: ApiError.() -> Unit = {}

    /**
     * finishRequest
     * */
    protected var finishRequest: (Boolean) -> Unit = {}

    /**
     * debug block
     * */
    protected var debug: (okhttp3.Response) -> Unit = {}


    /**
     *协程异常处理
     * */
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        err(ApiError(throwable))
    }

    /**
     *发起请求scope
     * */
    protected var onScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)

    /**
     * 处理response及err scope
     * */
    protected var downScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)

    /**
     * 取消请求
     * */
    val disposed: Disposed by lazy { Disposed() }


    /**
     * @param scope 执行请求scope
     * 加上SupervisorJob() 错误在兄弟协程之间隔离
     * */
    open fun onScope(scope: CoroutineScope): ApiContextImpl<V> {
        onScope = scope.plus(SupervisorJob())
        return this
    }

    /**
     * @param scope 执行响应scope
     * 加上SupervisorJob() 错误在兄弟协程之间隔离
     * */
    open fun downScope(scope: CoroutineScope): ApiContextImpl<V> {
        downScope = scope.plus(SupervisorJob())
        return this

    }

    /**
     * @param err
     * 错误回调，onscope 内发生的所以错误，除了本方法 error{ }中异常
     * */
    open fun error(err: ApiError.() -> Unit): ApiContextImpl<V> {
        this.err = err
        return this
    }

    /**
     * @param res
     * okHttp response 调试用
     * */
    open fun debug(res: okhttp3.Response.() -> Unit): ApiContextImpl<V> {
        this.debug = res
        return this
    }

    /**
     *@param finish
     * 请求结束时调用
     * */
    internal fun finishRequest(finish: (Boolean) -> Unit = {}): ApiContextImpl<V> {
        this.finishRequest = finish
        return this
    }

    /**
     * 取消请求
     * */
    open fun cancel() {
        if (job?.isActive == true) job?.cancel()
    }

    /**
     *自定义请求操作
     * */
    abstract infix fun down(success: V.() -> Unit): Disposed

    /**
     *开始请求
     * 不关系响应结果
     * */
    fun start(): Disposed {
        return down {}
    }

    /**
     *隔离对象，用于防止发起请求后再对请求环境进行操作
     * 只允许取消操作
     * */
    inner class Disposed {
        fun disposed() {
            cancel()
        }
    }

    /**
     *自定义错误包装类
     * */
    open class ApiError(val throwable: Throwable) {
        var code = -1
        var msg = ""
        var data: Any? = null
    }
}

sealed class ApiErr : Throwable() {
    /**
     *服务器端自定义错误
     * */
    object ServiceErr : ApiErr()

    /**
     * 请求成功，无响应内容
     * */
    object ServiceNoDataErr : ApiErr()
}