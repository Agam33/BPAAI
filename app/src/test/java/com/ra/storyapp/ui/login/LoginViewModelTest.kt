package com.ra.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.ra.storyapp.domain.usecase.StoryAppUseCase
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
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
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock private lateinit var useCase: StoryAppUseCase

    private lateinit var viewModel: LoginViewModel

    @get:Rule
    var mainCoroutinesRule = MainCoroutineRule()

    @Before
    fun setup() {
        viewModel = LoginViewModel(useCase)
    }

    @Test
    fun `saveVerification(), should be success`() = runTest {
        val returnValue = flow { emit(true) }
        doReturn(returnValue)
            .`when`(useCase).saveVerification(true)
        useCase.saveVerification(true)
        verify(useCase).saveVerification(true)
    }

    @Test
    fun `getVerification(), should return true`() = runTest {
        val status = flow { emit(true) }

       `when`(useCase.isVerified()).thenReturn(status)
        useCase.isVerified()
        verify(useCase).isVerified()

        viewModel.checkVerification()

        val expectedValue = useCase.isVerified().first()
        val actualValue = viewModel.isVerified.getOrAwaitValue().peekContent()
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `loginWithEmailAndPassword(), should be success`() = runTest {
        val loginResult = Resources.Success(DataDummy.getLoginResult())
        val flow = flow { emit(loginResult) }
        doReturn(flow)
            .`when`(useCase).register(DataDummy.name, DataDummy.email, DataDummy.password)
        useCase.register(DataDummy.name, DataDummy.email, DataDummy.password)
        verify(useCase).register(DataDummy.name, DataDummy.email, DataDummy.password)
    }

    @Test
    fun `loginRequest(), should return body response`() = runTest {
        val email = DataDummy.email
        val password = DataDummy.password

        val resources = Resources.Success(DataDummy.getLoginResult())
        val withFlow = flow { emit(resources) }

        `when`(useCase.login(email, password)).thenReturn(withFlow)
        useCase.login(email, password)
        verify(useCase).login(email, password)

        viewModel.loginWithEmailAndPassword(email, password)

        val expectedValue = useCase.login(email, password).last()
        val actualValue = viewModel.loginResult.getOrAwaitValue()

        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `saveToken(), should be success`() = runTest {
        val token = flow { emit(DataDummy.token)  }
        doReturn(token)
            .`when`(useCase).saveToken(DataDummy.token)
        useCase.saveToken(DataDummy.token)
        verify(useCase).saveToken(DataDummy.token)
    }

}
