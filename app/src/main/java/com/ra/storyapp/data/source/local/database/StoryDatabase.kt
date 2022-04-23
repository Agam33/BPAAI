package com.ra.storyapp.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ra.storyapp.data.source.local.database.entity.RemoteKeys
import com.ra.storyapp.data.source.local.database.entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeys::class],
    version = 2,
    exportSchema = true
)
abstract class StoryDatabase: RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}