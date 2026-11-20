package com.github.pvtitov.simplewishlist.ui.viewmodel

import com.github.pvtitov.simplewishlist.data.datasource.LocalDataSource
import com.github.pvtitov.simplewishlist.data.datasource.RemoteDataSource
import com.github.pvtitov.simplewishlist.data.model.FriendListDto
import com.github.pvtitov.simplewishlist.data.model.UserDto
import com.github.pvtitov.simplewishlist.data.model.WishDto
import com.github.pvtitov.simplewishlist.data.model.WishListDto
import com.github.pvtitov.simplewishlist.data.repository.LocalRepository
import com.github.pvtitov.simplewishlist.data.repository.RemoteRepositoryDefault
import com.github.pvtitov.simplewishlist.domain.interactor.Interactor
import com.github.pvtitov.simplewishlist.domain.interactor.InteractorDefault
import com.github.pvtitov.simplewishlist.domain.repository.RemoteRepository
import com.github.pvtitov.simplewishlist.ui.model.Wish
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AwlViewModelTests {

    private lateinit var viewModel: AwlViewModel
    private lateinit var interactor: Interactor
    private lateinit var localRepository: LocalRepository
    private lateinit var remoteRepository: RemoteRepository
    private lateinit var localDataSourceFake: LocalDataSource
    private lateinit var remoteDataSourceFake: RemoteDataSource

    @Test
    fun `WHEN login() successful THEN authorized`() = runTest {
        prepareViewModel(
            StandardTestDispatcher(testScheduler),
            InitData(shouldLogin = false)
        )

        assertFalse(viewModel.isLoadingFlow.value)
        assertFalse(viewModel.isAuthorizedFlow.value)
        val isAuthorizedJob = launch {
            viewModel.isAuthorizedFlow
                .first { isAuthorised -> isAuthorised }
        }
        val isLoadingJob = launch {
            viewModel.isLoadingFlow
                .first { isLoading -> !isLoading }
        }

        viewModel.loginWithGoogle()
        assertTrue(viewModel.isLoadingFlow.value)
        assertFalse(viewModel.isAuthorizedFlow.value)

        joinAll(isAuthorizedJob, isLoadingJob)
        assertFalse(viewModel.isLoadingFlow.value)
        assertTrue(viewModel.isAuthorizedFlow.value)
    }

    @Test
    fun `WHEN logout() successful THEN not authorized`() = runTest {
        prepareViewModel(
            StandardTestDispatcher(testScheduler)
        )

        assertFalse(viewModel.isLoadingFlow.value)
        assertTrue(viewModel.isAuthorizedFlow.value)
        val isAuthorizedJob = launch {
            viewModel.isAuthorizedFlow
                .first { isAuthorised -> !isAuthorised }
        }
        val isLoadingJob = launch {
            viewModel.isLoadingFlow
                .first { isLoading -> !isLoading }
        }

        viewModel.logout()
        assertTrue(viewModel.isLoadingFlow.value)
        assertTrue(viewModel.isAuthorizedFlow.value)

        joinAll(isAuthorizedJob, isLoadingJob)
        assertFalse(viewModel.isLoadingFlow.value)
        assertFalse(viewModel.isAuthorizedFlow.value)
    }

    @Test
    fun `GIVEN no local and one remote my wish WHEN load my wish list successfully THEN wish list of that wish emitted`() =
        runTest {
            prepareViewModel(
                StandardTestDispatcher(testScheduler),
                InitData(
                    myWishListRemote = WishListDto(
                        listOf(
                            WishDto("wish")
                        )
                    )
                )
            )

            assertFalse(viewModel.isLoadingFlow.value)
            assertTrue(viewModel.isAuthorizedFlow.value)
            assertEquals(emptyList(), viewModel.myWishListFlow.value)

            viewModel.loadMyWishList()
            assertTrue(viewModel.isLoadingFlow.value)
            viewModel.isLoadingFlow
                .first { isLoading -> !isLoading }

            assertEquals(listOf(Wish("wish", null, null, null)), viewModel.myWishListFlow.value)
        }

    private class InitData(
        val shouldLogin: Boolean = true,
        val myWishListLocal: WishListDto? = null,
        val myWishListRemote: WishListDto? = null,
    )

    private suspend fun prepareViewModel(
        testDispatcher: TestDispatcher,
        initData: InitData = InitData(),
    ) {
        localDataSourceFake = LocalDataSourceFake(
            myWishList = initData.myWishListLocal
        )
        remoteDataSourceFake = RemoteDataSourceFake(
            myWishList = initData.myWishListRemote
        )
        localRepository = LocalRepository(localDataSourceFake)
        remoteRepository = RemoteRepositoryDefault(remoteDataSourceFake)
        interactor = InteractorDefault(
            remoteRepository = remoteRepository,
            localRepository = localRepository,
        )
        viewModel = AwlViewModel(interactor, testDispatcher)

        if (initData.shouldLogin) {
            viewModel.loginWithGoogle()
            viewModel.isAuthorizedFlow.first { isAuthorized -> isAuthorized }
        }
    }

    private class LocalDataSourceFake(
        myWishList: WishListDto? = null
    ) : LocalDataSource {
        private var _user: UserDto? = null
        private var _myWishList: WishListDto? = myWishList
        private val _wishLists: MutableMap<String, WishListDto?> = mutableMapOf()
        private var _friendList: FriendListDto? = null

        override suspend fun login(user: UserDto) {
            this._user = user
        }

        override suspend fun getMyWishList(): WishListDto? {
            return _myWishList
        }

        override suspend fun getFriendsWishList(friendsShareToken: String): WishListDto? {
            return _wishLists[friendsShareToken]
        }

        override suspend fun saveMyWishList(wishList: WishListDto) {
            _myWishList = wishList
        }

        override suspend fun saveFriendsWishList(
            friendsShareToken: String,
            wishList: WishListDto
        ) {
            _wishLists[friendsShareToken] = wishList
        }

        override suspend fun addFriend(user: UserDto) {
            _friendList?.let { dto -> dto.copy(friendList = dto.friendList + user) }
                ?: FriendListDto(listOf(user))
        }

        override suspend fun getFriendList(): FriendListDto? {
            return _friendList
        }

        override suspend fun logout() {
            this._user = null
        }
    }

    private class RemoteDataSourceFake(
        myWishList: WishListDto? = null
    ) : RemoteDataSource {

        private var _user: UserDto? = UserDto("Abc", "123")
        private var _myWishList: WishListDto? = myWishList
        private val _wishLists: MutableMap<String, WishListDto?> = mutableMapOf()

        override suspend fun login(): UserDto {
            delay(2000L)
            return _user ?: error("login failed")
        }

        override suspend fun downloadMineWishList(): WishListDto? {
            delay(2000L)
            return _myWishList
        }

        override suspend fun downloadFriendsWishList(friendsShareToken: String): WishListDto? {
            delay(2000L)
            return _wishLists[friendsShareToken]
        }

        override suspend fun uploadMineWishList(wishList: WishListDto) {
            delay(2000L)
        }

        override suspend fun logout() {
            delay(2000L)
        }
    }
}