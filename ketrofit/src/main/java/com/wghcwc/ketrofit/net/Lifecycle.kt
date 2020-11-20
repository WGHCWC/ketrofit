package com.wghcwc.ketrofit.net

import androidx.lifecycle.*
import retrofit2.Call

/**
 * author：wghcwc
 * DATE: 20-11-14 18:12
 */

/**
 *  扩展储存LifecycleOwner 属性
 * */
private val LifecycleOwnerStore = HashMap<LifecycleOwner, HashMap<Any, Any>>()

val LifecycleOwner.tagMap: HashMap<Any, Any>
    get() {
        val map = LifecycleOwnerStore[this]
        if (map != null) {
            return map
        }
        val observer = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                LifecycleOwnerStore.remove(this@tagMap)
            }
        }
        lifecycle.addObserver(observer)
        val valueMap = HashMap<Any, Any>()
        LifecycleOwnerStore[this] = valueMap
        return valueMap
    }

inline fun <reified T> LifecycleOwner.getValue(key: Any): T? {
    val value = tagMap[key]
    return if (value is T) value else null
}

inline fun <reified T> LifecycleOwner.setValue(key: Any, value: T): T {
    this.tagMap[key] = value as Any
    return value
}


fun <T> LifecycleOwner.api(block: () -> Call<T>): ApiImpl<T> {
    return setApi(ApiImpl(block())) as ApiImpl<T>
}

fun <T> LifecycleOwner.api(call: Call<T>): ApiImpl<T> {
    return setApi(ApiImpl(call)) as ApiImpl<T>
}

fun <T> LifecycleOwner.setApi(flow: ApiContextImpl<T>): ApiContextImpl<T> {
    flow.downScope(lifecycleScope)
    flow.onScope(lifecycleScope)

    val observer = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            flow.cancel()
        }
    }
    lifecycle.addObserver(observer)
    flow.finishRequest {
        lifecycle.removeObserver(observer)
    }
    return flow
}

