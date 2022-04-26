package com.ra.storyapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story (
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var photoUrl: String = "",
    var createdAt: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
) : Parcelable
