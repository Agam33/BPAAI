package com.ra.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.ra.storyapp.domain.usecase.StoryAppUseCase
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
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
class RegisterViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock private lateinit var useCase: StoryAppUseCase

    private lateinit var viewModel: RegisterViewModel

    @get:Rule
    var mainCoroutinesRule = MainCoroutineRule()

    @Before
    fun setup() {
        viewModel = RegisterViewModel(useCase)
    }

    @Test
    fun `register(), should be success`() = runTest {
        val dataFlow = flow { emit(Resources.Success(DataDummy.getRegisterResponse())) }
        val name = DataDummy.name
        val email = DataDummy.email
        val password = DataDummy.password
        doReturn(dataFlow)
            .`when`(useCase).register(name, email, password)
        useCase.register(name, email, password)
        verify(useCase).register(name, email, password)
    }

    @Test
    fun `registerResult, error should be false and message should be success`() = runTest {
        val name = DataDummy.name
        val email = DataDummy.email
        val password = DataDummy.password

        val dataFlow = flow { emit(Resources.Success(DataDummy.getRegisterResponse())) }

        `when`(useCase.register(name, email, password)).thenReturn(dataFlow)
        useCase.register(name, email, password)
        verify(useCase).register(name, email, password)

        val dataResponse = useCase.register(name, email, password).last()
        val expectedError = dataResponse.data?.error
        val expectedMessage = dataResponse.data?.message

        viewModel.register(name, email, password)

        val actualData = viewModel.registerResult.getOrAwaitValue()

        assertEquals(expectedError, actualData.data?.error)
        assertEquals(expectedMessage, actualData.data?.message)
    }

}