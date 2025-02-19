package io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joaogouveia89.randomuser.R
import io.github.joaogouveia89.randomuser.core.di.IoDispatcher
import io.github.joaogouveia89.randomuser.core.internetConnectionMonitor.InternetConnectionMonitor
import io.github.joaogouveia89.randomuser.core.internetConnectionMonitor.InternetConnectionStatus
import io.github.joaogouveia89.randomuser.core.ktx.calculateOffset
import io.github.joaogouveia89.randomuser.core.ktx.hadPassedOneMinute
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepositoryFetchResponse
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserSaveState
import io.github.joaogouveia89.randomuser.randomUser.presentation.state.ErrorState
import io.github.joaogouveia89.randomuser.randomUser.presentation.state.LoadState
import io.github.joaogouveia89.randomuser.randomUser.presentation.state.UserProfileState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
    private val internetConnectionMonitor: InternetConnectionMonitor,
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

    private val internetConnectionStatus =
        internetConnectionMonitor
            .status
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                InternetConnectionStatus.OFFLINE
            )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            internetConnectionStatus.collect { connectionStatus ->
                _uiState.update {
                    if (connectionStatus == InternetConnectionStatus.OFFLINE) {
                        it.copy(errorState = ErrorState.Offline)
                    } else it
                }
            }
        }
    }

    private val getUserFlow = repository
        .getRandomUser()
        .map { userFetchState ->
            when (userFetchState) {
                is UserRepositoryFetchResponse.Loading -> {
                    if (_uiState.value.user == User())
                        UserProfileState(loadState = LoadState.GETTING_USER)
                    else {
                        _uiState.value.copy(loadState = LoadState.REPLACING_USER)
                    }
                }

                is UserRepositoryFetchResponse.Success -> {
                    startChronometer(userFetchState.user.timezoneOffset)
                    UserProfileState(
                        user = userFetchState.user,
                        errorState = if (
                            !userFetchState.isColorAnalysisError)
                            ErrorState.None
                        else
                            ErrorState.SnackBarError(R.string.error_message_color_analysis)
                    )
                }

                is UserRepositoryFetchResponse.SourceError -> {
                    if (isUserOnline()) {
                        _uiState.value.copy(
                            loadState = LoadState.IDLE,
                            errorState = ErrorState.SnackBarError(R.string.error_message_source)
                        )
                    } else _uiState.value.copy(
                        loadState = LoadState.IDLE,
                        errorState = ErrorState.Offline
                    )
                }
            }
        }

    fun execute(command: RandomUserCommand) {

        when (command) {
            is RandomUserCommand.GetNewUser -> getNewUser(command.clock)
            is RandomUserCommand.SaveUser -> saveUser()
            is RandomUserCommand.DismissError -> dismissError()
        }
    }

    private fun getNewUser(clock: Clock) {
        currentClock = clock
        viewModelScope.launch {
            getUserFlow.collect { userProfileState ->
                _uiState.value = userProfileState
            }
        }
    }

    private fun dismissError() {
        _uiState.update { it.copy(errorState = ErrorState.None) }
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
                    /* everytime a getNewUser is called, it cancels the coroutine and this exception block is called, so I just want to consider it
                     * as an error if the user has not change
                     */
                    if (isUserOnline() && chronJobUser == uiState.value.user) {
                        _uiState.update {
                            it.copy(errorState = ErrorState.SnackBarError(R.string.error_message_chronometer))
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

    private fun saveUser() {
        viewModelScope.launch {
            repository.saveUser(uiState.value.user).collect { saveState ->
                when (saveState) {
                    UserSaveState.Loading -> _uiState.update {
                        it.copy(isSaving = true)
                    }

                    is UserSaveState.Success -> {
                        _uiState.update {
                            it.copy(
                                user = it.user.copy(id = saveState.id)
                            )
                        }
                    }

                    is UserSaveState.Error -> {
                        _uiState.update { it.copy(errorState = ErrorState.SnackBarError(R.string.error_message_saving_user)) }
                    }
                }
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

    private fun isUserOnline() = internetConnectionStatus.value == InternetConnectionStatus.ONLINE

    companion object {
        private val TAG = this::class.java.name
    }
}