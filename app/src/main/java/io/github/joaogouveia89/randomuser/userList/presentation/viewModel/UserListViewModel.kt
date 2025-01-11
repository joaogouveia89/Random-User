package io.github.joaogouveia89.randomuser.userList.presentation.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joaogouveia89.randomuser.core.di.IoDispatcher
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListGetState
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListRepository
import io.github.joaogouveia89.randomuser.userList.presentation.state.UserListState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: UserListRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserListState())

    val uiState: StateFlow<UserListState>
        get() = _uiState

    init {
        viewModelScope.launch(dispatcher) {
            repository.getUsers().collect { state ->
                when(state){
                    is UserListGetState.Loading -> _uiState.update { UserListState(isLoading = true) }
                    is UserListGetState.Success -> _uiState.update { UserListState(userList = state.users) }
                    is UserListGetState.Error -> _uiState.update { UserListState(isError = true) }
                }
            }
        }
    }
}