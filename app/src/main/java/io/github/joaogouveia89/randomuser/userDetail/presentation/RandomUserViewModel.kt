package io.github.joaogouveia89.randomuser.userDetail.presentation

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

class RandomUserViewModel(
    repository: UserRepository
) : ViewModel() {
    private val locationTime = MutableStateFlow<Instant?>(null)

    private val userProfileRequest =
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
        locationTime
    ) { profileRequest, locationTime ->
        profileRequest.copy(locationTime = locationTime)
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = UserProfileState()
    )

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
}