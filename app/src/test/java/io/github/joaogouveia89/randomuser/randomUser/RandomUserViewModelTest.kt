package io.github.joaogouveia89.randomuser.randomUser

import app.cash.turbine.test
import io.github.joaogouveia89.randomuser.MainCoroutineRule
import io.github.joaogouveia89.randomuser.randomUser.data.repository.UserRepositoryImpl
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserFetchState
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserCommand
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
    private lateinit var viewModel: RandomUserViewModel

    @Test
    fun `uiState emits correct success state`() = runTest {

        val mockUser = User(id = 0, firstName = "John", lastName = "Doe")

        coEvery { mockRepository.getRandomUser() } returns flowOf(
            UserFetchState.Success(mockUser)
        )

        // Initialize viewModel
        viewModel = RandomUserViewModel(mockRepository, dispatcher)

        // Ensure all coroutines complete
        advanceUntilIdle()

        // Collect uiState and assert the expected result
        viewModel.uiState.test {
            skipItems(1) // skipping the initial value
            assertEquals(mockUser, awaitItem().user) // Verify correct user
        }
    }

//    @Test
//    fun `isSaveButtonEnabled is false when user id is non-zero`() = runTest {
//        // Set up initial state
//        val mockUser = User(id = 1)
//
//        coEvery { mockRepository.getRandomUser() } returns flowOf(
//            UserFetchState.Success(mockUser)
//        )
//
//        viewModel = RandomUserViewModel(mockRepository)
//
//        viewModel.execute(RandomUserCommand.GetNewUser)
//
//
//        viewModel.uiState.test {
//            val successState = awaitItem()
//            assertFalse(successState.isSaveButtonEnabled) // Verify correct user
//        }
//    }
}
