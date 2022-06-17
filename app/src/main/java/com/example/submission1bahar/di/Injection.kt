package com.example.submission1bahar.di

import android.content.Context
import com.example.submission1bahar.api.ApiClient
import com.example.submission1bahar.database.StoryDatabase
import com.example.submission1bahar.paging.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiClient.getApiService()
        return StoryRepository(database, apiService)
    }
}