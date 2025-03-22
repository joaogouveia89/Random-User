package io.github.joaogouveia89.randomuser.userList

import app.cash.turbine.test
import io.github.joaogouveia89.randomuser.MainCoroutineRule
import io.github.joaogouveia89.randomuser.core.presentation.screen.contentContainer.state.ContentState
import io.github.joaogouveia89.randomuser.core.model.User
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListGetState
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListRepository
import io.github.joaogouveia89.randomuser.userList.presentation.viewModel.UserListCommand
import io.github.joaogouveia89.randomuser.userList.presentation.viewModel.UserListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserListViewModelTest {

    private val testScheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(testScheduler)

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(dispatcher)

    private val mockRepository = mockk<UserListRepository>()
    private lateinit var viewModel: UserListViewModel

    private val users = listOf(
        User(id = 1, firstName = "John", lastName = "Doe", email = "john.doe@example.com"),
        User(id = 2, firstName = "Jane", lastName = "Smith", email = "jane.smith@example.com"),
        User(id = 3, firstName = "Alice", lastName = "Johnson", email = "alice.j@example.com")
    )


    @Test
    fun `uiState emits loading and success states when users are fetched`() = runTest {

        coEvery { mockRepository.getUsers() } returns flowOf(
            UserListGetState.Success(users)
        )

        viewModel = UserListViewModel(mockRepository, dispatcher)

        viewModel.execute(UserListCommand.GetUsers)

        advanceUntilIdle()

        viewModel.uiState.test {
            val successState = awaitItem()
            assertEquals(users, successState.userList)
        }
    }

    @Test
    fun `uiState emits error state when fetching users fails`() = runTest {
        coEvery { mockRepository.getUsers() } returns flowOf(
            UserListGetState.Error
        )

        viewModel = UserListViewModel(mockRepository, dispatcher)

        viewModel.execute(UserListCommand.GetUsers)

        advanceUntilIdle()

        viewModel.uiState.test {
            // Error state
            val errorState = awaitItem()

            // TODO CHANGE THIS TO GENERIC ERROR INSTEAD
            assertEquals(ContentState.Error(2131427346), errorState.contentState)
        }
    }

    @Test
    fun `search filters users by first name`() = runTest {

        coEvery { mockRepository.getUsers() } returns flowOf(
            UserListGetState.Success(users)
        )

        viewModel = UserListViewModel(mockRepository, dispatcher)

        viewModel.execute(UserListCommand.GetUsers)
        advanceUntilIdle()

        // Search by first name
        viewModel.execute(UserListCommand.Search("John"))

        // Simulate the delay of 100ms for the filtering process
        testScheduler.advanceTimeBy(100)

        viewModel.uiState.test {
            val searchState = awaitItem()
            assertEquals(1, searchState.userList.size)
            assertEquals(users.first(), searchState.userList.first())
        }
    }

    @Test
    fun `search returns full list when no matches found`() = runTest {

        coEvery { mockRepository.getUsers() } returns flowOf(
            UserListGetState.Success(users)
        )

        viewModel = UserListViewModel(mockRepository, dispatcher)

        viewModel.execute(UserListCommand.GetUsers)
        advanceUntilIdle()

        // Search with no matches
        viewModel.execute(UserListCommand.Search("NotFound"))

        viewModel.uiState.test {
            val searchState = awaitItem()
            assertEquals(users, searchState.userList) // Returns full list on no match
        }
    }
}
