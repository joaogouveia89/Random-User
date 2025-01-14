package io.github.joaogouveia89.randomuser.randomUser

import app.cash.turbine.test
import io.github.joaogouveia89.randomuser.MainCoroutineRule
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepositoryFetchResponse
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserSaveState
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserCommand
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RandomUserViewModelTest {

    // Create a TestCoroutineScheduler only once
    private val testScheduler = TestCoroutineScheduler()

    // Create a dispatcher that uses the test scheduler
    private val dispatcher = StandardTestDispatcher(testScheduler)

    // Set the dispatcher in the MainCoroutineRule so it's used in all tests
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(dispatcher)

    private val mockRepository = mockk<UserRepository>()
    private val clock = mockk<Clock>(relaxed = true)
    private lateinit var viewModel: RandomUserViewModel

    @Test
    fun `uiState emits correct success state`() = runTest {

        val mockUser = User(id = 0, firstName = "John", lastName = "Doe")

        coEvery { mockRepository.getRandomUser() } returns flowOf(
            UserRepositoryFetchResponse.Success(mockUser)
        )

        // Initialize viewModel
        viewModel = RandomUserViewModel(mockRepository, dispatcher)

        viewModel.execute(RandomUserCommand.GetNewUser(clock))

        // Ensure all coroutines complete
        advanceUntilIdle()

        // Collect uiState and assert the expected result
        viewModel.uiState.test {
            skipItems(1) // skipping the initial value
            assertEquals(mockUser, awaitItem().user) // Verify correct user
        }
    }

    @Test
    fun `isSaveButtonEnabled is false when user id is non-zero`() = runTest {
        // Set up initial state
        val mockUser = User(id = 1)

        coEvery { mockRepository.getRandomUser() } returns flowOf(
            UserRepositoryFetchResponse.Success(mockUser)
        )

        viewModel = RandomUserViewModel(mockRepository, dispatcher)

        viewModel.execute(RandomUserCommand.GetNewUser(Clock.System))

        // Ensure all coroutines complete
        advanceUntilIdle()

        viewModel.uiState.test {
            skipItems(1) // skipping the initial value
            val successState = awaitItem()
            assertFalse(successState.isSaveButtonEnabled) // Verify correct user
        }
    }

    @Test
    fun `uiState emits loading state when repository is fetching user`() = runTest {
        coEvery { mockRepository.getRandomUser() } returns flowOf(
            UserRepositoryFetchResponse.Loading
        )

        viewModel = RandomUserViewModel(mockRepository, dispatcher)

        viewModel.execute(RandomUserCommand.GetNewUser(clock))

        advanceUntilIdle()

        viewModel.uiState.test {
            skipItems(1) // skipping the initial value
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading) // Verify loading state
        }
    }

    @Test
    fun `uiState emits error state when repository fetch fails`() = runTest {
        val errorMes = "Network error"
        coEvery { mockRepository.getRandomUser() } returns flowOf(
            UserRepositoryFetchResponse.SourceError(errorMes)
        )

        viewModel = RandomUserViewModel(mockRepository, dispatcher)

        viewModel.execute(RandomUserCommand.GetNewUser(clock))

        advanceUntilIdle()

        viewModel.uiState.test {
            skipItems(1) // skipping the initial value
            val errorState = awaitItem()
            assertEquals(User(), errorState.user) // Verify user is reset to default
            assertFalse(errorState.isLoading) // Ensure not loading
            assertEquals(errorMes, errorState.errorMessage) // Ensure error set
        }
    }

    @Test
    fun `saveUser updates user id on success`() = runTest {
        val mockUser = User(id = 0, firstName = "John", lastName = "Doe")

        coEvery { mockRepository.getRandomUser() } returns flowOf(
            UserRepositoryFetchResponse.Success(mockUser)
        )

        coEvery { mockRepository.saveUser(mockUser) } returns flowOf(
            UserSaveState.Success(123L)
        )
        viewModel = RandomUserViewModel(mockRepository, dispatcher)

        // Trigger fetching a new user
        viewModel.execute(RandomUserCommand.GetNewUser(Clock.System))
        advanceUntilIdle()

        // Assert that the user was updated in uiState
        viewModel.uiState.test {
            skipItems(1)
            val stateAfterFetch = awaitItem()
            assertEquals(mockUser, stateAfterFetch.user) // Ensure correct user is loaded
            cancelAndIgnoreRemainingEvents()
        }

        // Trigger saving the user
        viewModel.execute(RandomUserCommand.SaveUser)
        advanceUntilIdle()

        // Assert that the user ID was updated after saving
        viewModel.uiState.test {
            val stateAfterSave = awaitItem()
            assertEquals(123L, stateAfterSave.user.id) // Verify user ID is updated
            cancelAndIgnoreRemainingEvents()
        }
    }

//    @Test
//    fun `chronometer starts and updates locationTime correctly`() = runTest {
//        // Create a TestCoroutineDispatcher for time control
//        val testDispatcher = StandardTestDispatcher()
//
//        // Mock getRandomUser to return a user with a timezone offset
//        val offset = "+01:00"
//        val mockUser = User(timezoneOffset = offset)
//
//        coEvery { mockRepository.getRandomUser() } returns flowOf(
//            UserFetchState.Success(mockUser)
//        )
//
//        val mockClock = mockk<Clock>()
//        every { mockClock.now() } returns Instant.fromEpochMilliseconds(testScheduler.currentTime)
//
//        // Create ViewModel and inject the test dispatcher
//        viewModel = RandomUserViewModel(mockRepository, testDispatcher)
//
//        viewModel.execute(RandomUserCommand.GetNewUser(mockClock))
//
//        // Start testing the UI state
//        viewModel.uiState.test {
//            // Skip initial state if necessary
//            skipItems(1)
//
//            // Advance time by 2 minutes (this is under your test control)
//            testScheduler.advanceTimeBy(2.minutes)
//
//            // Collect the updated state after time has advanced
//            val updatedState = awaitItem()
//
//            // Check if locationTime was updated correctly after 2 minutes
//            assertEquals(mockClock.now().plus(2.minutes), updatedState.locationTime)
//
//            // Cancel the test scheduler to stop the test
//            testScheduler.cancel()
//        }
//    }

}
