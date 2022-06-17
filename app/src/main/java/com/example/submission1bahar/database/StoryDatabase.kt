package com.example.submission1bahar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.submission1bahar.viewmodel.ListStoryItem


@Database(
    entities = [ListStoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase:RoomDatabase() {
    companion object{
        @Volatile
        private var INSTANCE:StoryDatabase?=null

        @JvmStatic
        fun getDatabase(context: Context):StoryDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?:Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java,"stori_esapp"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}