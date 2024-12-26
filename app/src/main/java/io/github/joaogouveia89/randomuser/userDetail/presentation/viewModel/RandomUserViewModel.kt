package io.github.joaogouveia89.randomuser.userDetail.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.joaogouveia89.randomuser.core.ktx.calculateOffset
import io.github.joaogouveia89.randomuser.domain.repository.UserFetchState
import io.github.joaogouveia89.randomuser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.userDetail.presentation.state.UserProfileState
import kotlinx.coroutines.Dispatchers
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
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

// https://medium.com/@domen.lanisnik/pull-to-refresh-with-compose-material-3-26b37dbea966

class RandomUserViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private val locationTime = MutableStateFlow<Instant?>(null)
    private val refreshUser = MutableStateFlow<UserFetchState?>(null)

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
                            startChronometer(
                                Clock.System.now().calculateOffset(it)
                            )
                        }

                        UserProfileState(
                            isLoading = false,
                            user = randomUserResponse.user
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
                    isGettingNewUser = true
                )

                is UserFetchState.Success -> profileRequest.copy(
                    isGettingNewUser = false,
                    user = refreshedState.user
                )
            }
        } ?: profileRequest

        newState.copy(locationTime = locationTime)
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

    private fun startChronometer(start: Instant) {
        viewModelScope.launch(Dispatchers.IO) {
            var currentInst = start
            while (true) {
                delay(1000)
                currentInst = currentInst.plus(1.seconds)
                locationTime.emit(currentInst)
            }
        }
    }

    private fun getNewUser() {
        viewModelScope.launch {
            refreshUser.emitAll(
                repository.getRandomUser()
            )
        }
    }
}