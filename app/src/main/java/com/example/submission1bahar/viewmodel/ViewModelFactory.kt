package com.example.submission1bahar.viewmodel

import android.app.Application
import android.content.Context

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submission1bahar.camera.ViewModelAddstory
import com.example.submission1bahar.di.Injection
import com.example.submission1bahar.preferences.UserPreference

class ViewModelFactory(private val pref: UserPreference,private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    private lateinit var mApplication: Application

    fun setApplication(application: Application) {
        mApplication = application
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(pref) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref,Injection.provideRepository(context)) as T
        }
        if (modelClass.isAssignableFrom(ViewModelAddstory::class.java)) {
            return ViewModelAddstory(pref) as T
        }
        if (modelClass.isAssignableFrom(ViewModelLogout::class.java)) {
            return ViewModelLogout(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}