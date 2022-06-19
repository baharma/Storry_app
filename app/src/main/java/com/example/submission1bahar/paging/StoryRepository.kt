package com.example.submission1bahar.paging

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.submission1bahar.api.ApiInterface
import com.example.submission1bahar.database.StoryDatabase
import com.example.submission1bahar.viewmodel.ListStoryItem

class StoryRepository(private val storyDatabase: StoryDatabase
,private val apiInterface: ApiInterface) {
 fun getAllStories(token:String):LiveData<PagingData<ListStoryItem>>{
return Pager(
    config = PagingConfig(
        pageSize = 5
    ),
    pagingSourceFactory = {
        StorryPagingSource(apiInterface,token)
    }
).liveData
}

}