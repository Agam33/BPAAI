package com.ra.storyapp.data.source.local

import androidx.paging.PagingSource
import com.ra.storyapp.data.source.local.database.entity.StoryEntity

interface ILocalDataSource {
    fun getStories(): PagingSource<Int, StoryEntity>
}