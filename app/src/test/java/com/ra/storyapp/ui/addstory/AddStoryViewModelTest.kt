package com.ra.storyapp.ui.addstory

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ra.storyapp.domain.usecase.IStoryAppUseCase
import com.ra.storyapp.domain.usecase.StoryAppUseCase
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import storyapp.DataDummy
import storyapp.MainCoroutineRule
import storyapp.getOrAwaitValue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var useCase: StoryAppUseCase
    @Mock private lateinit var location: Location

    private lateinit var viewModel: AddStoryViewModel

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        viewModel = AddStoryViewModel(useCase)

    }

    @Test
    fun `fileUploadResponse, should be success`() = runTest {
        val dataFlow = flow { emit(Resources.Success(DataDummy.getFileUploadResponse())) }
        `when`(useCase.addNewStory(
            "${DataDummy.BEARER_TOKEN} ${DataDummy.token}",
            DataDummy.getFile(),
            DataDummy.description,
            DataDummy.latitude.toFloat(),
            DataDummy.longitude.toFloat()
        )).thenReturn(dataFlow)
        useCase.addNewStory(
            "${DataDummy.BEARER_TOKEN} ${DataDummy.token}",
            DataDummy.getFile(),
            DataDummy.description,
            DataDummy.latitude.toFloat(),
            DataDummy.longitude.toFloat()
        )
        verify(useCase).addNewStory("${DataDummy.BEARER_TOKEN} ${DataDummy.token}", DataDummy.getFile(), DataDummy.description, DataDummy.latitude.toFloat(), DataDummy.longitude.toFloat())

        val flowToken = flow { emit(DataDummy.token) }
        `when`(useCase.getToken()).thenReturn(flowToken)
        useCase.getToken()
        verify(useCase).getToken()

        val response = useCase.addNewStory(
            "${DataDummy.BEARER_TOKEN} ${DataDummy.token}",
            DataDummy.getFile(), DataDummy.description,
            DataDummy.latitude.toFloat(), DataDummy.longitude.toFloat()).last()

        viewModel.addNewStory(
            DataDummy.getFile(),
            DataDummy.description,
            DataDummy.latitude.toFloat(),
            DataDummy.longitude.toFloat()
        )

        val expectedError = response.data?.error
        val expectedMessage = response.data?.message

        val viewModelData = viewModel.fileUploadResponse.getOrAwaitValue().data
        val actualError = viewModelData?.error
        val actualMessage = viewModelData?.message

        assertEquals(expectedMessage, actualMessage)
        assertEquals(expectedError, actualError)
    }

    @Test
    fun `getCurrentLocation, should be success`()  {
        `when`(location.latitude).thenReturn(DataDummy.latitude)
        `when`(location.longitude).thenReturn(DataDummy.longitude)

        val expectedLatitude = DataDummy.latitude.toFloat()
        val expectedLongitude = DataDummy.longitude.toFloat()

        viewModel.setLocation(location)

        val data = viewModel.getCurrentLocation.getOrAwaitValue()
        val actualLatitude = data.latitude.toFloat()
        val actualLongitude = data.longitude.toFloat()

        assertEquals(expectedLatitude, actualLatitude)
        assertEquals(expectedLongitude, actualLongitude)
    }
}