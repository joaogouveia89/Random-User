package io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joaogouveia89.randomuser.R
import io.github.joaogouveia89.randomuser.core.internetConnectionMonitor.InternetConnectionMonitor
import io.github.joaogouveia89.randomuser.core.internetConnectionMonitor.InternetConnectionStatus
import io.github.joaogouveia89.randomuser.core.ktx.calculateOffset
import io.github.joaogouveia89.randomuser.core.presentation.screen.contentContainer.state.ContentState
import io.github.joaogouveia89.randomuser.core.model.User
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepositoryFetchResponse
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserSaveState
import io.github.joaogouveia89.randomuser.randomUser.presentation.state.UserProfileState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

private const val CACHE_LIFETIME_MS = 5000L

@HiltViewModel
class RandomUserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val clock: Clock,
    internetConnectionMonitor: InternetConnectionMonitor
) : ViewModel() {
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

    private val getUserFlow = repository
        .getRandomUser()
        .map { userFetchState ->
            when (userFetchState) {
                is UserRepositoryFetchResponse.Loading -> {
                    if (uiState.value.user == User())
                        UserProfileState(contentState = ContentState.Loading)
                    else {
                        _uiState.value.copy(isReplacingUser = true)
                    }
                }

                is UserRepositoryFetchResponse.Success -> {
                    UserProfileState(
                        user = userFetchState.user,
                        locationTime = clock
                            .now()
                            .calculateOffset(userFetchState.user.timezoneOffset),
                        contentState = if (
                            !userFetchState.isColorAnalysisError)
                            ContentState.Ready
                        else
                            ContentState.Error(R.string.error_message_color_analysis)
                    )
                }

                is UserRepositoryFetchResponse.SourceError -> {
                    if (isUserOnline()) {
                        _uiState.value.copy(
                            isReplacingUser = false,
                            contentState = ContentState.Error(R.string.error_message_source),
                            showSnackBar = !_uiState.value.shouldShowFullErrorScreen()
                        )
                    } else _uiState.value.copy(
                        isReplacingUser = false,
                        contentState = ContentState.Offline,
                        showSnackBar = !_uiState.value.shouldShowFullErrorScreen()
                    )
                }
            }
        }

    fun execute(command: RandomUserCommand) {

        when (command) {
            is RandomUserCommand.MonitorInternetStatus -> monitorInternetStatus()
            is RandomUserCommand.GetNewUser -> getNewUser()
            is RandomUserCommand.SaveUser -> saveUser()
            is RandomUserCommand.DismissError -> dismissError()
            is RandomUserCommand.ErrorRetryClick -> getNewUser()
            is RandomUserCommand.OnLocalClockUpdated -> updateUserLocalTime()
        }
    }

    private fun monitorInternetStatus(){
        viewModelScope.launch(Dispatchers.IO) {
            internetConnectionStatus.collect { connectionStatus ->
                _uiState.update {
                    if (connectionStatus == InternetConnectionStatus.OFFLINE) {
                        it.copy(
                            contentState = ContentState.Offline,
                            showSnackBar = !it.shouldShowFullErrorScreen()
                        )
                    } else if (it.user == User()) {
                        getNewUser()
                        it
                    } else it
                }
            }
        }
    }

    private fun getNewUser() {
        viewModelScope.launch {
            getUserFlow.collect { userProfileState ->
                _uiState.update { userProfileState }
            }
        }
    }

    private fun updateUserLocalTime() {
        if(_uiState.value.contentState == ContentState.Ready){
            _uiState.update {
                it.copy(
                    locationTime = clock.now().calculateOffset(it.user.timezoneOffset)
                )
            }
        }
    }

    private fun dismissError() {
        _uiState.update { it.copy(showSnackBar = false, contentState = ContentState.Ready) }
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
                        _uiState.update {
                            it.copy(
                                contentState = ContentState.Error(R.string.error_message_saving_user),
                                showSnackBar = true
                            )
                        }
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

    private fun isUserOnline() = internetConnectionStatus.value == InternetConnectionStatus.ONLINE
}