package com.ra.storyapp.data.source.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ra.storyapp.data.source.local.database.entity.StoryEntity

@Dao
interface StoryDao {

    @Query("SELECT * FROM story_table")
    fun getStories(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story_table")
    suspend fun deleteStories()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)
}