package storyapp

import com.ra.storyapp.data.source.remote.network.response.RegisterResponse
import com.ra.storyapp.domain.model.LoginResult
import android.location.Location
import android.location.LocationManager
import com.ra.storyapp.R
import com.ra.storyapp.data.source.local.database.entity.StoryEntity
import com.ra.storyapp.data.source.remote.network.response.FileUploadResponse
import com.ra.storyapp.domain.model.Story
import java.io.File

object DataDummy {
    const val email = "example@gmail.com"
    const val password = "123"
    const val name = "name"
    const val token = "myTokenXxx"
    const val latitude = -6.8957643
    const val longitude = 107.6338462
    const val description: String = "blabla"
    const val BEARER_TOKEN = "Bearer"
    const val  mapStyle: Int = R.raw.dark_map_style

    fun getLoginResult(): LoginResult =
        LoginResult("123", "example", "123456")

    fun getFileUploadResponse(): FileUploadResponse =
        FileUploadResponse(false, "success")

    fun getRegisterResponse(): RegisterResponse =
        RegisterResponse(false, "success")

    fun getFile(): File = File("path/dummyFile")

    fun listStory(): List<Story> {
        val stories = ArrayList<Story>()
        for(i in 0..10) {
            stories.add(
                Story(
                    "$i",
                    "name-$i",
                    description = description,
                    photoUrl = "image/path/dummy.jpg",
                    createdAt = "28-04-2022",
                    latitude,
                    longitude
                )
            )
        }
        return stories
    }

    fun listStoryEntities(): List<StoryEntity> {
        val stories = ArrayList<StoryEntity>()
        for(i in 0..10) {
            stories.add(
                StoryEntity(
                    "$i",
                    "name-$i",
                    description = description,
                    photoUrl = "image/path/dummy.jpg",
                    createdAt = "28-04-2022",
                    latitude,
                    longitude
                )
            )
        }
        return stories
    }
}