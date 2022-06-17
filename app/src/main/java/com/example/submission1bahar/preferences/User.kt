package com.example.submission1bahar.preferences

import com.google.gson.annotations.SerializedName

data class User (
    @field:SerializedName("userId")
    val userId: String?=null,
    @field:SerializedName("name")
    val name: String?=null,
    @field:SerializedName("token")
    val token: String?=null
    )