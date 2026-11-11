package com.github.pvtitov.simplewishlist.ui.viewmodel

import com.github.pvtitov.simplewishlist.data.datasource.LocalDataSource
import com.github.pvtitov.simplewishlist.data.datasource.RemoteDataSource
import com.github.pvtitov.simplewishlist.data.model.FriendListDto
import com.github.pvtitov.simplewishlist.data.model.UserDto
import com.github.pvtitov.simplewishlist.data.model.WishListDto
import com.github.pvtitov.simplewishlist.data.repository.LocalRepository
import com.github.pvtitov.simplewishlist.data.repository.RemoteRepositoryDefault
import com.github.pvtitov.simplewishlist.domain.interactor.Interactor
import com.github.pvtitov.simplewishlist.domain.interactor.InteractorDefault
import com.github.pvtitov.simplewishlist.domain.repository.RemoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class AwlViewModelTests {

    private lateinit var viewModel: AwlViewModel
    private lateinit var interactor: Interactor
    private lateinit var localRepository: LocalRepository
    private lateinit var remoteRepository: RemoteRepository
    private lateinit var localDataSourceFake: LocalDataSource
    private lateinit var remoteDataSourceFake: RemoteDataSource

    @Before
    fun setup() {
        localDataSourceFake = LocalDataSourceFake()
        remoteDataSourceFake = RemoteDataSourceFake()
        localRepository = LocalRepository(localDataSourceFake)
        remoteRepository = RemoteRepositoryDefault(remoteDataSourceFake)
        interactor = InteractorDefault(
            remoteRepository = remoteRepository,
            localRepository = localRepository,
        )
    }

    @Test
    fun `WHEN login() successful THEN authorized and loading stops`() = runTest {
        viewModel = AwlViewModel(interactor, UnconfinedTestDispatcher(testScheduler))

        Assert.assertFalse(viewModel.isLoadingFlow.value)
        Assert.assertFalse(viewModel.isAuthorizedFlow.value)
        val isAuthorizedJob = launch {
            viewModel.isAuthorizedFlow
                .first { isAuthorised -> isAuthorised }
        }
        val isLoadingJob = launch {
            viewModel.isLoadingFlow
                .first { isLoading -> !isLoading }
        }

        viewModel.loginWithGoogle()
        Assert.assertTrue(viewModel.isLoadingFlow.value)
        Assert.assertFalse(viewModel.isAuthorizedFlow.value)

        joinAll(isAuthorizedJob, isLoadingJob)
        Assert.assertFalse(viewModel.isLoadingFlow.value)
        Assert.assertTrue(viewModel.isAuthorizedFlow.value)
    }

    @Test
    fun `WHEN logout() successful THEN not authorized and loading stops`() = runTest {
        viewModel = AwlViewModel(interactor, UnconfinedTestDispatcher(testScheduler))
        viewModel.loginWithGoogle()
        viewModel.isAuthorizedFlow.first { isAuthorized -> isAuthorized}

        Assert.assertFalse(viewModel.isLoadingFlow.value)
        Assert.assertTrue(viewModel.isAuthorizedFlow.value)
        val isAuthorizedJob = launch {
            viewModel.isAuthorizedFlow
                .first { isAuthorised -> !isAuthorised }
        }
        val isLoadingJob = launch {
            viewModel.isLoadingFlow
                .first { isLoading -> !isLoading }
        }

        viewModel.logout()
        Assert.assertTrue(viewModel.isLoadingFlow.value)
        Assert.assertTrue(viewModel.isAuthorizedFlow.value)

        joinAll(isAuthorizedJob, isLoadingJob)
        Assert.assertFalse(viewModel.isLoadingFlow.value)
        Assert.assertFalse(viewModel.isAuthorizedFlow.value)
    }

    private class LocalDataSourceFake : LocalDataSource {
        private var user: UserDto? = null
        private var myWishList: WishListDto? = null
        private val wishLists: MutableMap<String, WishListDto?> = mutableMapOf()
        private var friendList: FriendListDto? = null

        override suspend fun login(user: UserDto) {
            this.user = user
        }

        override suspend fun getMyWishList(): WishListDto? {
            return myWishList
        }

        override suspend fun getFriendsWishList(friendsShareToken: String): WishListDto? {
            return wishLists[friendsShareToken]
        }

        override suspend fun saveMyWishList(wishList: WishListDto) {
            myWishList = wishList
        }

        override suspend fun saveFriendsWishList(
            friendsShareToken: String,
            wishList: WishListDto
        ) {
            wishLists[friendsShareToken] = wishList
        }

        override suspend fun addFriend(user: UserDto) {
            friendList?.let { dto -> dto.copy(friendList = dto.friendList + user) }
                ?: FriendListDto(listOf(user))
        }

        override suspend fun getFriendList(): FriendListDto? {
            return friendList
        }

        override suspend fun logout() {
            this.user = null
        }
    }

    private class RemoteDataSourceFake : RemoteDataSource {
        override suspend fun login(): UserDto {
            delay(2000L)
            return UserDto("Abc", "123")
        }

        override suspend fun downloadMineWishList(): WishListDto? {
            delay(2000L)
            return null
        }

        override suspend fun downloadFriendsWishList(friendsShareToken: String): WishListDto? {
            delay(2000L)
            return null
        }

        override suspend fun uploadMineWishList(wishList: WishListDto) {
            delay(2000L)
        }

        override suspend fun logout() {
            delay(2000L)
        }
    }
}