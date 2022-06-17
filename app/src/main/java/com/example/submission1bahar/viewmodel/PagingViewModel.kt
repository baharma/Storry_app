package com.example.submission1bahar.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submission1bahar.di.Injection
import com.example.submission1bahar.paging.StoryRepository
import java.lang.IllegalArgumentException

class PagingViewModel(private val storyRepository: StoryRepository):ViewModel() {

//    fun getTheListStories(token: String): LiveData<PagingData<ListStoryItem>> =
//        storyRepository.getAllStories(token)
//        .cachedIn(viewModelScope)
//
//    class ViewModelFactory(private val context: Context):ViewModelProvider.Factory{
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if(modelClass.isAssignableFrom(PagingViewModel::class.java)){
//                @Suppress("UNCHECKED_CAST")
//                return PagingViewModel(Injection.provideRepository(context)) as T
//            }
//            else throw IllegalArgumentException("Unknown ViewModel class")
//        }
//
//    }
}