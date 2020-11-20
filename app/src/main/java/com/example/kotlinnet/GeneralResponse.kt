package com.example.kotlinnet

/**
 * author：wghcwc
 * DATE: 20-11-14 15:53
 */

data class GeneralResponse<T>(
    val code: Int,
    val msg: String,
    val model: T
)