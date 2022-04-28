package storyapp

import com.ra.storyapp.data.source.remote.network.response.RegisterResponse
import com.ra.storyapp.domain.model.LoginResult
import android.location.Location
import android.location.LocationManager
import com.ra.storyapp.data.source.remote.network.response.FileUploadResponse
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

    fun getLoginResult(): LoginResult =
        LoginResult("123", "example", "123456")

    fun getFileUploadResponse(): FileUploadResponse =
        FileUploadResponse(false, "success")

    fun getRegisterResponse(): RegisterResponse =
        RegisterResponse(false, "success")

    fun getLocation(): Location  {
        val location = Location(LocationManager.GPS_PROVIDER)
        location.latitude = latitude
        location.longitude = longitude
        return location
    }

    fun getFile(): File = File("path/dummyFile")

}