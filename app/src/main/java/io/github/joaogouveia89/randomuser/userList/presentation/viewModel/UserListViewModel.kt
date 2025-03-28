package io.github.joaogouveia89.randomuser.userList.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joaogouveia89.randomuser.R
import io.github.joaogouveia89.randomuser.core.di.IoDispatcher
import io.github.joaogouveia89.randomuser.core.presentation.screen.contentContainer.state.ContentState
import io.github.joaogouveia89.randomuser.core.model.User
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListGetState
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListRepository
import io.github.joaogouveia89.randomuser.userList.domain.repository.UsersDeleteState
import io.github.joaogouveia89.randomuser.userList.presentation.state.UserListState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LEVENSHTEIN_DISTANCE_MAX = 3

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: UserListRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserListState())

    private var fullUserList: List<Pair<User, Boolean>> = listOf()

    val uiState: StateFlow<UserListState>
        get() = _uiState

    fun execute(command: UserListCommand) {
        when (command) {
            UserListCommand.GetUsers -> getUsers()
            UserListCommand.MultipleDeleteClick -> multipleDelete()
            is UserListCommand.Search -> search(command.query)
            is UserListCommand.UserLongClick -> userLongClickHandle(command.user)
            is UserListCommand.UserClick -> userClick(command.user)
        }
    }

    private fun getUsers() {
        viewModelScope.launch(dispatcher) {
            repository.getUsers().collect { state ->
                when (state) {
                    is UserListGetState.Loading -> _uiState.update { UserListState(contentState = ContentState.Loading) }
                    is UserListGetState.Success -> {
                        fullUserList = state.users.map { Pair(it, false) }
                        _uiState.update { UserListState(userList = state.users.map { Pair(it, false) }) }
                    }

                    is UserListGetState.Error -> _uiState.update {
                        UserListState(
                            showSnackBar = true,
                            contentState = ContentState.Error(R.string.error_generic_description)
                        )
                    }
                }
            }
        }
    }

    private fun multipleDelete(){
        viewModelScope.launch(dispatcher) {
            val usersToDelete = uiState.value.userList.filter { it.second }
            repository.deleteUsers(usersToDelete.map { it.first }).collect{ state ->
                if(state is UsersDeleteState.Success){
                    _uiState.update {
                        it.copy(
                            userList = uiState.value.userList - usersToDelete.toSet(),
                            isMultiSelectionMode = false
                        )
                    }
                }
            }
        }
    }

    private fun userClick(user: User){
        val selectedCount = _uiState.value.userList.count { it.second }
        if(selectedCount > 0){
            selectUser(user)
        } else{
            _uiState.update {
                it.copy(
                    askedForDetails = user
                )
            }
        }
    }

    private fun userLongClickHandle(user: User) {
        selectUser(user)
    }

    private fun selectUser(user: User){
        val newList = _uiState.value.userList.map {
            if(it.first == user){
                Pair(user, !it.second)
            } else it
        }
        _uiState.update {
            it.copy(
                userList = newList,
                isMultiSelectionMode = !newList.all { pair -> !pair.second }
            )
        }
    }

    /*
    * in this specific case, I chose to do all the mapping here, since the data is already loaded into memory,
    * and therefore would avoid doing another search in the repository for something that is already in memory,
    * if it was a search that involved an API, in that case it would make sense to use the repository for that
    */
    private fun search(query: String) {
        viewModelScope.launch(dispatcher) {
            _uiState.update {
                it.copy(
                    userList = if (query.isEmpty())
                        fullUserList
                    else filterList(fullUserList, query).ifEmpty { fullUserList }
                )
            }
        }
    }

    private suspend fun filterList(users: List<Pair<User, Boolean>>, query: String): List<Pair<User, Boolean>> {
        val chunkedUsers =
            users.chunked(100) // To avoid coroutines overhead, using one to evaluate blocks of 100

        val jobs = chunkedUsers.map { chunk ->
            viewModelScope.async(dispatcher) {
                chunk.filter { user ->
                    listOf(
                        levenshteinDistance(user.first.firstName, query),
                        levenshteinDistance(user.first.lastName, query),
                        levenshteinDistance(user.first.email, query)
                    ).any {
                        it < LEVENSHTEIN_DISTANCE_MAX
                    }
                }
            }
        }

        return jobs.awaitAll().flatten()
    }


    // thanks for https://gist.github.com/ademar111190/34d3de41308389a0d0d8
    private fun levenshteinDistance(lhs: CharSequence, rhs: CharSequence): Int {
        val lhsLength = lhs.length + 1
        val rhsLength = rhs.length + 1

        var cost = Array(lhsLength) { it }
        var newCost = Array(lhsLength) { 0 }

        for (i in 1..<rhsLength) {
            newCost[0] = i

            for (j in 1..<lhsLength) {
                val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1

                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1

                newCost[j] = costInsert.coerceAtMost(costDelete).coerceAtMost(costReplace)
            }

            val swap = cost
            cost = newCost
            newCost = swap
        }

        return cost[lhsLength - 1]
    }

}