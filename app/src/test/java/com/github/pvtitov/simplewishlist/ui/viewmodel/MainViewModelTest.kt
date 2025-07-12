package com.github.pvtitov.simplewishlist.ui.viewmodel

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import com.github.pvtitov.simplewishlist.data.ManualRepository
import com.github.pvtitov.simplewishlist.domain.model.Credentials
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.ui.model.LoginScreen
import com.github.pvtitov.simplewishlist.utils.DI
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.function.ThrowingRunnable

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

//    private lateinit var viewModel: MainViewModel
//    private lateinit var contextMock: Context
//
//    @Before
//    fun setup() {
//        val mainDispatcherTest = UnconfinedTestDispatcher()
//        Dispatchers.setMain(mainDispatcherTest)
//        contextMock = mockk(relaxed = true)
//        val activityMock: ComponentActivity = mockk(relaxed = true)
//        val lifecycleMock: Lifecycle = mockk(relaxed = true)
//        every { activityMock.lifecycle } returns lifecycleMock
//        every { lifecycleMock.currentState } returns Lifecycle.State.INITIALIZED
//        mockkConstructor(ManualRepository::class)
//        every { ManualRepository(activityMock) } returns mockk(relaxed = true)
//        DI.init(activityMock)
//
//        viewModel = MainViewModel()
//    }
//
//    @After
//    fun tearDown() {
//        clearAllMocks()
//    }
//
//    @Test
//    fun when_created_then_initial_state() {
//        assertEquals(false, viewModel.isWishListUpdatedState.value)
//        assertEquals(null, viewModel.errorState.value)
//        assertEquals(LoginScreen, viewModel.currentScreenState.value)
//        assertEquals(null, viewModel.currentLoginFlow.value)
//    }
//
//    @Test
//    fun when_not_authorized_actions_allowed() {
//        assertError { viewModel.onClickUsers() }
//        assertError { viewModel.onClickWishList() }
//        assertError { viewModel.onClickDownload() }
//        assertError { viewModel.onClickUpload(contextMock) }
//        assertError { viewModel.onClickNewWish() }
//        assertError { viewModel.onClickSaveNewWish(null, Wish("")) }
//        assertError { viewModel.onClickWish(Wish("")) }
//        assertError { viewModel.onClickEditWish(Wish("")) }
//        assertError { viewModel.onClickDeleteWish(Wish("")) }
//        assertError { viewModel.onClickConfirmDeleteWish(Wish("")) }
//        assertError { viewModel.onClickUser(User("")) }
//
//        viewModel.onClickLogin()
//        viewModel.onClickSubmitLogin(Credentials("", ""))
//    }
//
//    @Test
//    fun when_authorized_actions_allowed() = runTest {
//        viewModel.ioDispatcher = StandardTestDispatcher()
//
//        val notEmptyLogin = "testLogin"
//        viewModel.onClickSubmitLogin(Credentials(notEmptyLogin, ""))
//
//        yield()
//
////        assertEquals(true, viewModel.currentScreenState.value is WishListScreen)
//
//        viewModel.onClickUsers()
//        viewModel.onClickWishList()
//        viewModel.onClickDownload()
//        viewModel.onClickUpload(contextMock)
//        viewModel.onClickNewWish()
//        viewModel.onClickSaveNewWish(null, Wish(""))
//        viewModel.onClickWish(Wish(""))
//        viewModel.onClickEditWish(Wish(""))
//        viewModel.onClickDeleteWish(Wish(""))
//        viewModel.onClickConfirmDeleteWish(Wish(""))
//        viewModel.onClickUser(User(""))
//        viewModel.onClickLogin()
//        viewModel.onClickSubmitLogin(Credentials("", ""))
//    }
//
//    private fun assertError(action: ThrowingRunnable) {
//        assertThrows(IllegalStateException::class.java, action)
//    }
}