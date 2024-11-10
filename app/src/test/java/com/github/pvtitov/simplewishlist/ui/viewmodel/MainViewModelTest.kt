package com.github.pvtitov.simplewishlist.ui.viewmodel

import com.github.pvtitov.simplewishlist.domain.model.Credentials
import com.github.pvtitov.simplewishlist.ui.model.LoginScreen
import com.github.pvtitov.simplewishlist.ui.model.UsersScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = MainViewModel()
        viewModel.setIoDispatcher(Dispatchers.Unconfined)
    }

    @Test
    fun `when viewmodel is created login value is null`() = runTest {
        assertNull(viewModel.currentLoginFlow.value)
    }

    @Test
    fun `first screen viewmodel starts with is login screen`() = runTest {
        assertEquals(LoginScreen, viewModel.currentScreenState.value)
    }

    @Test
    fun `right after successful login show empty users screen`() = runTest {
        viewModel.onClickSubmitLogin(Credentials("", ""))
        advanceUntilIdle()

        assertEquals(UsersScreen(emptyList()), viewModel.currentScreenState.value)
    }

    @Test
    fun `when users button clicked and there is no data then show empty users screen`() = runTest {
        viewModel.onClickSubmitLogin(Credentials("", ""))
        advanceUntilIdle()

        viewModel.onClickUsers()

        assertEquals(UsersScreen(emptyList()), viewModel.currentScreenState.value)
    }

    @Test
    fun `when data successfully downloaded then show users screen with users on it`() = runTest {
        viewModel.onClickSubmitLogin(Credentials("", ""))
        advanceUntilIdle()

        val repository = TestRepository()
        viewModel.setManualAccountDataSource(repository)
        viewModel.onClickDownload()
        advanceUntilIdle()

        val expectedUsers = repository.import().data.map { it.user }
        assertEquals(UsersScreen(expectedUsers), viewModel.currentScreenState.value)
    }
}