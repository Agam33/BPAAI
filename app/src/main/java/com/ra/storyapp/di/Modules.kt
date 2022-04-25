package com.ra.storyapp.di

import com.ra.storyapp.data.StoryRepository
import com.ra.storyapp.data.source.remote.IRemoteDataSource
import com.ra.storyapp.data.source.remote.RemoteDataSource
import com.ra.storyapp.data.source.remote.network.ApiService
import com.ra.storyapp.domain.repository.IStoryRepository
import com.ra.storyapp.domain.usecase.IStoryAppUseCase
import com.ra.storyapp.domain.usecase.StoryAppUseCase
import com.ra.storyapp.ui.addstory.AddStoryViewModel
import com.ra.storyapp.ui.liststory.ListStoryViewModel
import com.ra.storyapp.ui.login.LoginViewModel
import com.ra.storyapp.ui.maps.MapsViewModel
import com.ra.storyapp.ui.register.RegisterViewModel
import com.ra.storyapp.utils.BASE_URL
import com.ra.storyapp.utils.preferences.IUserPreferencesStore
import com.ra.storyapp.utils.preferences.UserPreferencesStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    single {
       val retrofit = Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .client(get())
           .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single<IStoryRepository> { StoryRepository(get()) }
    single<IRemoteDataSource> { RemoteDataSource(get()) }
}

val useCaseModule = module {
    factory<IStoryAppUseCase> { StoryAppUseCase(get(), get()) }
}

val viewModelModule = module {
    factory { AddStoryViewModel(get()) }
    factory { ListStoryViewModel(get()) }
    factory { RegisterViewModel(get()) }
    factory { LoginViewModel(get()) }
    factory { MapsViewModel(get()) }
}

val dataStoreModule = module {
    single<IUserPreferencesStore> { UserPreferencesStore(androidContext()) }
}
