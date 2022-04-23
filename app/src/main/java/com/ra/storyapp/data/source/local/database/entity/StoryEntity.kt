package com.ra.storyapp.data.source.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("story_table")
data class StoryEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("description")
    val description: String,

    @ColumnInfo("photoUrl")
    val photoUrl: String,

    @ColumnInfo("createdAt")
    val createdAt: String,

    @ColumnInfo("lat")
    val latitude: Double,

    @ColumnInfo("lon")
    val longitude: Double,
)