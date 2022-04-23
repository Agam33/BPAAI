package com.ra.storyapp.data.source.local

import androidx.paging.PagingSource
import com.ra.storyapp.data.source.local.database.StoryDao
import com.ra.storyapp.data.source.local.database.entity.StoryEntity

class LocalDataSource(
    private val storyDao: StoryDao
): ILocalDataSource {
    override fun getStories(): PagingSource<Int, StoryEntity> = storyDao.getStories()
}