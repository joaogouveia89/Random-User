package io.github.joaogouveia89.randomuser.userDetail.presentation.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joaogouveia89.randomuser.R
import io.github.joaogouveia89.randomuser.core.di.IoDispatcher
import io.github.joaogouveia89.randomuser.core.ktx.calculateOffset
import io.github.joaogouveia89.randomuser.core.ktx.hadPassedOneMinute
import io.github.joaogouveia89.randomuser.core.presentation.navigation.DetailScreenNav.Companion.USER_ID
import io.github.joaogouveia89.randomuser.userDetail.domain.repository.UserDetailDeleteState
import io.github.joaogouveia89.randomuser.userDetail.domain.repository.UserDetailGetState
import io.github.joaogouveia89.randomuser.userDetail.domain.repository.UserDetailRepository
import io.github.joaogouveia89.randomuser.userDetail.presentation.state.UserDetailState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

private const val CACHE_LIFETIME_MS = 5000L

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    val repository: UserDetailRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val userId = savedStateHandle.get<Long>(key = USER_ID)

    private var chronJob: Job? = null
    private lateinit var currentClock: Clock
    private val _uiState = MutableStateFlow(UserDetailState())

    val uiState: StateFlow<UserDetailState>
        get() = _uiState


    fun execute(command: UserDetailCommand) {
        when (command) {
            is UserDetailCommand.GetUserDetails -> getUserDetails(command.clock)
            is UserDetailCommand.DeleteUser -> deleteUser()
            is UserDetailCommand.ConfirmDeleteDialog -> confirmDeleteUser()
            is UserDetailCommand.DismissDeleteDialog -> {
                _uiState.update {
                    it.copy(showDeleteDialog = false)
                }
            }
            is UserDetailCommand.DismissError -> {}
        }
    }

    private fun getUserDetails(clock: Clock){
        userId?.let {
            currentClock = clock
            viewModelScope.launch {
                repository.getUser(it).collect{ getState ->
                    when(getState){
                        is UserDetailGetState.Loading -> _uiState.update {
                            UserDetailState(isLoading = true)
                        }
                        is UserDetailGetState.Success -> _uiState.update {
                            startChronometer(getState.user.timezoneOffset)
                            UserDetailState(user = getState.user)
                        }
                        is UserDetailGetState.Error -> {}
                    }
                }
            }
        }
    }

    private fun deleteUser(){
        _uiState.update {
            it.copy(showDeleteDialog = true)
        }
    }

    private fun confirmDeleteUser(){
        _uiState.update {
            it.copy(showDeleteDialog = true)
        }

        viewModelScope.launch(dispatcher) {
            // FIXME uiState.value.user.id is comming as 0 for some reason
            repository.deleteUser(uiState.value.user.id).collect{
                if(it is UserDetailDeleteState.Success){
                    _uiState.update { state ->
                        state.copy(
                            navigateBack = true
                        )
                    }
                }
            }
        }
    }

    private fun startChronometer(offset: String) {
        viewModelScope.launch(dispatcher) {
            stopChronometer()

            chronJob = viewModelScope.launch(dispatcher) {
                val chronJobUser = uiState.value.user
                try {
                    var currentInst = currentClock.now().calculateOffset(offset)
                    _uiState.update {
                        it.copy(
                            locationTime = currentInst
                        )
                    }
                    while (isActive) {
                        delay(1.seconds)
                        currentInst = currentInst.plus(1.seconds)
                        if (currentInst.hadPassedOneMinute(_uiState.value.locationTime ?: currentInst)) {
                            _uiState.update { state ->
                                state.copy(
                                    locationTime = currentInst
                                )
                            }
                        }
                    }
                } catch (exception: Exception) {
                    /* everytime a getNewUser is called, it cancels the coroutine and this exception block is called, so I just want to consider it
                     * as an error if the user has not change
                     */
                    if (chronJobUser == uiState.value.user) {
                        _uiState.update {
                            it.copy(errorMessage = R.string.error_message_chronometer)
                        }
                        Log.e(TAG, "chronJob has stopped due to ${exception.message}")
                    }
                }
            }
        }
    }

    private suspend fun stopChronometer() {
        chronJob?.let { job ->
            /*
             * Avoiding racing conditions, so it ensures the coroutine is
             * finished before assigning another one to chronJob
             */
            job.cancelAndJoin()
            chronJob = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            stopChronometer()
        }
    }

    companion object {
        private val TAG = this::class.java.name
    }
}