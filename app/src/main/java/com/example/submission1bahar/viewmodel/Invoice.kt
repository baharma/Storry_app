package com.example.submission1bahar.viewmodel

sealed class Invoice <T>(val data: T?  = null, val message: String? = null) {

    class Success<T>(data: T?) : Invoice<T>(data)
    class Error<T>(message: String?, data: T? = null) : Invoice<T>(data, message)
    class Loading<T>(data: T? = null) : Invoice<T>(data)

}