package com.ra.storyapp.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.verify
import com.ra.storyapp.domain.usecase.StoryAppUseCase
import com.ra.storyapp.utils.MapStyleOption
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import storyapp.DataDummy
import storyapp.MainCoroutineRule
import storyapp.getOrAwaitValue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock private lateinit var useCase: StoryAppUseCase

    private lateinit var viewModel: MapsViewModel

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        viewModel = MapsViewModel(useCase)
    }

    @Test
    fun `getAllStoriesWithLocation(), should be success`() = runTest {
        val flowToken = flow { emit(DataDummy.token) }
        val flowListStory = flow { emit(Resources.Success(DataDummy.listStory())) }
        val token = flowToken.first()
        `when`(useCase.getToken()).thenReturn(flowToken)
        `when`(useCase.getAllStoriesWithLocation("${DataDummy.BEARER_TOKEN} $token"))
            .thenReturn(flowListStory)

        useCase.getToken()
        useCase.getAllStoriesWithLocation("${DataDummy.BEARER_TOKEN} $token")
        verify(useCase).getToken()
        verify(useCase).getAllStoriesWithLocation("${DataDummy.BEARER_TOKEN} $token")

        val expectedListStory = useCase.getAllStoriesWithLocation("${DataDummy.BEARER_TOKEN} $token").first()

        viewModel.getAllStoriesWithLocation()

        val actualListStory = viewModel.getAllStoriesWithLocation().getOrAwaitValue().data

        assertEquals(expectedListStory.data?.size, actualListStory?.size)
        assertEquals(expectedListStory.data?.get(0)?.id, actualListStory?.get(0)?.id)
    }

    @Test
    fun `getMapStyle(), should be success`() {
        viewModel.setMapStyle(MapStyleOption.DARK)

        val expectedStyle = DataDummy.mapStyle
        val actualStyle = viewModel.getMapStyle.getOrAwaitValue()

        assertEquals(expectedStyle, actualStyle)
    }
}