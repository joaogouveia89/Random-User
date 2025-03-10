package io.github.joaogouveia89.randomuser.randomUser

import app.cash.turbine.test
import io.github.joaogouveia89.randomuser.MainCoroutineRule
import io.github.joaogouveia89.randomuser.core.fakeData.fakeUser
import io.github.joaogouveia89.randomuser.core.internetConnectionMonitor.InternetConnectionMonitor
import io.github.joaogouveia89.randomuser.core.service.remote.UserService
import io.github.joaogouveia89.randomuser.core.service.remote.model.RandomUserResponse
import io.github.joaogouveia89.randomuser.randomUser.data.repository.UserRepositoryImpl
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepositoryFetchResponse
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserSaveState
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserCommand
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RandomUserViewModelTest {
    private val testScheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(testScheduler)

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(dispatcher)

    private val mockRepository = mockk<UserRepository>(relaxed = true)
    private val internetConnectionMonitor = mockk<InternetConnectionMonitor>(relaxed = true)
    private val fixedInstant = Instant.parse("2024-03-10T12:00:00Z")
    private val clock = FakeClock(fixedInstant)
    private lateinit var viewModel: RandomUserViewModel

    @Test
    fun `uiState emits correct success state`() = runTest {

        coEvery { mockRepository.getRandomUser() } returns flowOf(UserRepositoryFetchResponse.Success(fakeUser))

        // Initialize viewModel
        viewModel = RandomUserViewModel(
            repository = mockRepository,
            clock = clock,
            internetConnectionMonitor = internetConnectionMonitor
        )


        viewModel.execute(RandomUserCommand.GetNewUser)

        // Collect uiState and assert the expected result
        viewModel.uiState.test {
            skipItems(1)
            assertEquals(fakeUser, awaitItem().user) // Verify correct user
        }
    }

    @Test
    fun `isSaveButtonEnabled is false when user id is non-zero`() = runTest {
        viewModel = RandomUserViewModel(
            repository = mockRepository,
            clock = clock,
            internetConnectionMonitor = internetConnectionMonitor
        )

        coEvery { mockRepository.saveUser(any()) } returns flowOf(UserSaveState.Success(123))

        viewModel.execute(RandomUserCommand.SaveUser)

        viewModel.uiState.test {
            skipItems(1)
            val successState = awaitItem()
            assertFalse(successState.isSaveButtonEnabled)
        }
    }

    // TODO ANTES DE TRABALHAR NESSE TESTE MEXER NO [Instant.kt](file://Users/joaogouveia/AndroidStudioProjects/RandomUser/app/src/main/java/io/github/joaogouveia89/randomuser/core/ktx/Instant.kt)
    // TODO calculateOffset TEM QUE CONSIDERAR OFFSET INVALIDO, FAZER UM REGEX PRA ISSO, ALEM DISSO, TESTAR ISSO TAMBEM
    @Test
    fun `uiState emits isReplacingUser as true when there is already an user on view model`() =
        runTest {

            viewModel = RandomUserViewModel(
                repository = mockRepository,
                clock = clock,
                internetConnectionMonitor = internetConnectionMonitor
            )

            coEvery { mockRepository.getRandomUser() } returns flowOf(
                UserRepositoryFetchResponse.Success(fakeUser)
            )

            viewModel.execute(RandomUserCommand.GetNewUser)

            advanceUntilIdle()

            coEvery { mockRepository.getRandomUser() } returns flowOf(
                UserRepositoryFetchResponse.Loading
            )

            viewModel.execute(RandomUserCommand.GetNewUser)

            advanceUntilIdle()

            viewModel.uiState.test {
                val item = awaitItem()
                assertEquals(fakeUser, item.user)
                assertEquals(true, awaitItem().isReplacingUser) // Verify loading state
            }
        }

    @Test
    fun `uiState emits error state when repository fetch fails`() = runTest {

//        coEvery { mockRepository.getRandomUser() } returns flowOf(
//            UserRepositoryFetchResponse.SourceError
//        )
//
//        coEvery { internetConnectionMonitor.status } returns MutableStateFlow(
//            InternetConnectionStatus.ONLINE
//        )
//
//        viewModel = RandomUserViewModel(
//            repository = mockRepository,
//            clock = clock,
//            internetConnectionMonitor = internetConnectionMonitor
//        )
//
//        viewModel.execute(RandomUserCommand.GetNewUser)
//
//        advanceUntilIdle()
//
//        viewModel.uiState.test {
//            skipItems(1) // skipping the initial value
//            val errorState = awaitItem()
//            assertEquals(User(), errorState.user) // Verify user is reset to default
//            assertEquals(LoadState.IDLE, errorState.loadState) // Ensure not loading
//            assertEquals(
//                ErrorState.SnackBarError(R.string.error_message_source),
//                errorState.errorState
//            ) // Ensure error set
//        }
    }

    @Test
    fun `saveUser updates user id on success`() = runTest {
        viewModel = RandomUserViewModel(
            repository = mockRepository,
            clock = clock,
            internetConnectionMonitor = internetConnectionMonitor
        )

        coEvery { mockRepository.saveUser(any()) } returns flowOf(
            UserSaveState.Success(123L)
        )

        //Trigger saving the user
        viewModel.execute(RandomUserCommand.SaveUser)

        // Assert that the user ID was updated after saving
        viewModel.uiState.test {
            skipItems(1)
            val stateAfterSave = awaitItem()
            assertEquals(123L, stateAfterSave.user.id) // Verify user ID is updated
            cancelAndIgnoreRemainingEvents()
        }
    }
}
