package com.ra.storyapp.data.source.local

import androidx.paging.PagingSource
import com.ra.storyapp.data.source.local.database.entity.StoryEntity
import com.ra.storyapp.domain.model.Story

interface ILocalDataSource {
    fun getStories(): PagingSource<Int, StoryEntity>
}