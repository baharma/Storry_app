package com.example.submission1bahar.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.submission1bahar.api.ApiClient
import com.example.submission1bahar.preferences.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelMaps(private val pref : UserPreference): ViewModel() {

    private val _getAllstory = MutableLiveData<List<ListStoryItem>>()
    private val getAllStories : LiveData<List<ListStoryItem>> = _getAllstory

    fun getToken(): LiveData<String> {
        return pref.getUserAuth().asLiveData()
    }

     fun getListMapsAll(token:String){
     val client = ApiClient.getApiService().getAllMapsStory(token)
         client.enqueue(object : Callback<GetAllStories>{
             override fun onResponse(call: Call<GetAllStories>, response: Response<GetAllStories>) {
                 if (response.isSuccessful){
                     _getAllstory.value = response.body()?.listStory
                 }else{
                     Log.d("onFailure", response.body()?.message.toString())
                 }
             }

             override fun onFailure(call: Call<GetAllStories>, t: Throwable) {
                 Log.d("onFailure", t.message.toString())
             }
         })
    }

    fun getallmaps():LiveData<List<ListStoryItem>>{
        return getAllStories
    }
}