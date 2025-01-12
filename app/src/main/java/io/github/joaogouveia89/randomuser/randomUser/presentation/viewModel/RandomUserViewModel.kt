package io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joaogouveia89.randomuser.core.di.IoDispatcher
import io.github.joaogouveia89.randomuser.core.ktx.calculateOffset
import io.github.joaogouveia89.randomuser.core.ktx.hadPassedOneMinute
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepositoryResponse
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserSaveState
import io.github.joaogouveia89.randomuser.randomUser.presentation.state.UserProfileState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

private const val CACHE_LIFETIME_MS = 5000L

@HiltViewModel
class RandomUserViewModel @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private var chronJob: Job? = null
    private lateinit var currentClock: Clock
    private val _uiState = MutableStateFlow(UserProfileState())

    val uiState: StateFlow<UserProfileState> =
        _uiState.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(stopTimeoutMillis = CACHE_LIFETIME_MS),
            initialValue = UserProfileState()
        )

    private val refreshUserFlow = repository
        .getRandomUser()
        .map { userFetchState ->
            when (userFetchState) {
                is UserRepositoryResponse.Loading -> {
                    if (_uiState.value.user == User())
                        UserProfileState(isLoading = true)
                    else {
                        _uiState.value.copy(
                            isGettingNewUser = true,
                            errorMessage = null
                        )
                    }
                }

                is UserRepositoryResponse.Success -> {
                    startChronometer(userFetchState.user.timezoneOffset)
                    UserProfileState(user = userFetchState.user)
                }

                is UserRepositoryResponse.SourceError -> {
                    _uiState.value.copy(errorMessage = userFetchState.message)
                }
            }
        }

    fun execute(command: RandomUserCommand) {
        when (command) {
            is RandomUserCommand.GetNewUser -> getNewUser(command.clock)
            is RandomUserCommand.SaveUser -> saveUser()
        }
    }

    private fun getNewUser(clock: Clock) {
        currentClock = clock
        viewModelScope.launch {
            refreshUserFlow.collect { userProfileState ->
                _uiState.value = userProfileState
            }
        }
    }

    private fun startChronometer(offset: String) {
        viewModelScope.launch(dispatcher) {
            stopChronometer()

            chronJob = viewModelScope.launch(dispatcher) {
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
                        _uiState.update { state ->
                            if (currentInst.hadPassedOneMinute(state.locationTime ?: currentInst)) {
                                state.copy(
                                    locationTime = currentInst
                                )
                            } else {
                                state
                            }
                        }
                    }
                } catch (exception: Exception) {
                    Log.e(TAG, "chronJob has stopped due to ${exception.message}")
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

    private fun saveUser() {
        viewModelScope.launch {
            repository.saveUser(uiState.value.user).collect { saveState ->
                if (saveState is UserSaveState.Success) {
                    _uiState.update {
                        it.copy(
                            user = it.user.copy(id = saveState.id)
                        )
                    }
                }
            }
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