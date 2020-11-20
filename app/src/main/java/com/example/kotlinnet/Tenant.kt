package com.example.kotlinnet

/**
 * author：wghcwc
 * DATE: 20-11-12 15:51
 */


data class Tenant(
    val id: Int,
    val tenantId: Long,
    val backDomain: String,
    val frontDomain: String,
    val isDeleted: Boolean
)
