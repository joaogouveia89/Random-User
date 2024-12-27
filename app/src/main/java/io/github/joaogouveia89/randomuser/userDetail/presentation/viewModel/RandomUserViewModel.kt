package io.github.joaogouveia89.randomuser.userDetail.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.joaogouveia89.randomuser.core.ktx.calculateOffset
import io.github.joaogouveia89.randomuser.core.ktx.hadPassedOneMinute
import io.github.joaogouveia89.randomuser.core.ktx.humanizedHourMin
import io.github.joaogouveia89.randomuser.userDetail.domain.model.User
import io.github.joaogouveia89.randomuser.userDetail.domain.repository.UserFetchState
import io.github.joaogouveia89.randomuser.userDetail.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.userDetail.presentation.state.UserProfileState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

class RandomUserViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private var chronJob: Job? = null
    private val locationTime = MutableStateFlow(Clock.System.now())
    private val refreshUser = MutableStateFlow<UserFetchState?>(null)
    private var currentUser: User = User()

    private var userProfileRequest =
        repository
            .getRandomUser()
            .map { randomUserResponse ->
                when (randomUserResponse) {
                    is UserFetchState.Loading -> {
                        UserProfileState(isLoading = true)
                    }

                    is UserFetchState.Success -> {
                        randomUserResponse.user.timezoneOffset.let {
                            startChronometer(it)
                        }

                        currentUser = randomUserResponse.user

                        UserProfileState(
                            isLoading = false,
                            user = currentUser
                        )
                    }

                    is UserFetchState.Error -> {
                        UserProfileState()
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(),
                initialValue = UserProfileState()
            )


    val uiState: StateFlow<UserProfileState> = combine(
        userProfileRequest,
        locationTime,
        refreshUser
    ) { profileRequest, locationTime, refreshedUser ->
        val newState = refreshedUser?.let { refreshedState ->
            when (refreshedState) {
                is UserFetchState.Error -> profileRequest
                UserFetchState.Loading -> profileRequest.copy(
                    isGettingNewUser = true,
                    user = currentUser
                )

                is UserFetchState.Success -> {
                    currentUser = refreshedState.user
                    startChronometer(refreshedState.user.timezoneOffset)
                    profileRequest.copy(
                        isGettingNewUser = false,
                        user = currentUser
                    )
                }
            }
        } ?: profileRequest

        newState.copy(locationTime = locationTime.humanizedHourMin())
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = UserProfileState()
    )

    fun execute(command: RandomUserCommand) {
        when (command) {
            is RandomUserCommand.GetNewUser -> getNewUser()
        }
    }

    private fun startChronometer(offset: String) {
        stopChronometer()
        val start = Clock.System.now().calculateOffset(offset)
        chronJob = viewModelScope.launch(Dispatchers.IO) {
            var currentInst = start
            locationTime.emit(start)
            while (true) {
                delay(1000)
                currentInst = currentInst.plus(1.seconds)
                if (currentInst.hadPassedOneMinute()) {
                    locationTime.emit(currentInst)
                }
            }
        }
    }

    private fun stopChronometer() {
        chronJob?.cancel()
        chronJob = null
    }

    private fun getNewUser() {
        viewModelScope.launch {
            refreshUser.emitAll(
                repository.getRandomUser()
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopChronometer()
    }
}