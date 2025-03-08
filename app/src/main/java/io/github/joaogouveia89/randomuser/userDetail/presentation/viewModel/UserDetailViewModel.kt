package io.github.joaogouveia89.randomuser.userDetail.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joaogouveia89.randomuser.core.di.IoDispatcher
import io.github.joaogouveia89.randomuser.core.ktx.calculateOffset
import io.github.joaogouveia89.randomuser.core.presentation.navigation.DetailScreenNav.Companion.USER_ID
import io.github.joaogouveia89.randomuser.userDetail.domain.repository.UserDetailDeleteState
import io.github.joaogouveia89.randomuser.userDetail.domain.repository.UserDetailGetState
import io.github.joaogouveia89.randomuser.userDetail.domain.repository.UserDetailRepository
import io.github.joaogouveia89.randomuser.userDetail.presentation.state.UserDetailState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    val repository: UserDetailRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val clock: Clock,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val userId = savedStateHandle.get<Long>(key = USER_ID)

    private val _uiState = MutableStateFlow(UserDetailState())

    val uiState: StateFlow<UserDetailState>
        get() = _uiState


    fun execute(command: UserDetailCommand) {
        when (command) {
            is UserDetailCommand.GetUserDetails -> getUserDetails()
            is UserDetailCommand.DeleteUser -> deleteUser()
            is UserDetailCommand.ConfirmDeleteDialog -> confirmDeleteUser()
            is UserDetailCommand.DismissDeleteDialog -> {
                _uiState.update {
                    it.copy(showDeleteDialog = false)
                }
            }

            is UserDetailCommand.OnLocalClockUpdated -> updateUserLocalTime()

            is UserDetailCommand.DismissError -> {}
        }
    }

    private fun getUserDetails() {
        userId?.let {
            viewModelScope.launch {
                repository.getUser(it).collect { getState ->
                    when (getState) {
                        is UserDetailGetState.Loading -> _uiState.update {
                            UserDetailState(isLoading = true)
                        }

                        is UserDetailGetState.Success -> _uiState.update {
                            UserDetailState(
                                user = getState.user,
                                locationTime = clock.now()
                                    .calculateOffset(getState.user.timezoneOffset),
                            )
                        }

                        is UserDetailGetState.Error -> {}
                    }
                }
            }
        }
    }

    private fun updateUserLocalTime() {
        _uiState.update {
            it.copy(
                locationTime = clock.now().calculateOffset(it.user.timezoneOffset)
            )
        }
    }

    private fun deleteUser() {
        _uiState.update {
            it.copy(showDeleteDialog = true)
        }
    }

    private fun confirmDeleteUser() {
        _uiState.update {
            it.copy(showDeleteDialog = true)
        }

        viewModelScope.launch(dispatcher) {
            // FIXME uiState.value.user.id is comming as 0 for some reason
            repository.deleteUser(uiState.value.user.id).collect {
                if (it is UserDetailDeleteState.Success) {
                    _uiState.update { state ->
                        state.copy(
                            navigateBack = true
                        )
                    }
                }
            }
        }
    }
}