package com.example.submission1bahar.database

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.submission1bahar.api.ApiInterface
import com.example.submission1bahar.viewmodel.ListStoryItem


//@OptIn(ExperimentalPagingApi::class)
//class StoryRemoteMediator(
//    private val token: String,
//    private val database: StoryDatabase,
//    private val apiService: ApiInterface
//): RemoteMediator<Int, ListStoryItem>() {
//
//    private companion object {
//        const val INITIAL_PAGE_INDEX = 1
//    }
//    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
//    }
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, ListStoryItem>
//    ): MediatorResult {
//        val page = INITIAL_PAGE_INDEX
//    }

//}