package com.example.submission1bahar.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.submission1bahar.preferences.UserPreference
import kotlinx.coroutines.launch

class ViewModelLogout(private val pref: UserPreference) : ViewModel() {
    private lateinit var viewModel: AuthViewModel

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

    fun getToken(): LiveData<String> {
        return pref.getUserAuth().asLiveData()
    }


}