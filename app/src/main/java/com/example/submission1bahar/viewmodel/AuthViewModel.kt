package com.example.submission1bahar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission1bahar.api.ApiClient
import com.example.submission1bahar.preferences.ErrorResponse
import com.example.submission1bahar.preferences.LoginResponse
import com.example.submission1bahar.preferences.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val pref: UserPreference) : ViewModel() {
    private val _authmodel = MutableLiveData<Invoice<String>>()
    val authmodel: LiveData<Invoice<String>> = _authmodel


    fun login(email: String, password: String) {
        _authmodel.postValue(Invoice.Loading())
        val client = ApiClient.getApiService().loginUser(email, password)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResult = response.body()?.loginResult?.token

                    loginResult?.let { saveUserAuth(it) }

                    _authmodel.postValue(Invoice.Success(loginResult))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ErrorResponse::class.java
                    )
                    _authmodel.postValue(Invoice.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(
                    AuthViewModel::class.java.simpleName,
                    "onFailure login"
                )
                _authmodel.postValue(Invoice.Error(t.message))
            }
        })
    }


    fun register(name: String, email: String, password: String) {
        _authmodel.postValue(Invoice.Loading())
        val client = ApiClient.getApiService().registerUser(name, email, password)

        client.enqueue(object : Callback<ErrorResponse> {
            override fun onResponse(call: Call<ErrorResponse>, response: Response<ErrorResponse>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message.toString()
                    _authmodel.postValue(Invoice.Success(message))
                } else {
                    val errordata = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ErrorResponse::class.java
                    )
                    _authmodel.postValue(Invoice.Error(errordata.message))
                }
            }

            override fun onFailure(call: Call<ErrorResponse>, t: Throwable) {
                Log.e(
                    AuthViewModel::class.java.simpleName,
                    "onFailure register"
                )
                _authmodel.postValue(Invoice.Error(t.message))
            }

        })

    }

    fun saveUserAuth(user: String) {
        viewModelScope.launch {
            pref.saveUserAuth(user)
        }
    }

    fun logout() = deleteAuth()

    fun deleteAuth() {
        viewModelScope.launch {
            pref.removeUserAuth()
        }
    }

    fun getUserAuth() {
        viewModelScope.launch {
            pref.getUserAuth()
        }
    }

}


