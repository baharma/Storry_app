package com.example.submission1bahar.preferences

import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    @field:SerializedName("error")
    val error: Boolean?,
    @field:SerializedName("message")
    val message: String?,
)