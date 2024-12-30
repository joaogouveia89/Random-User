package io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joaogouveia89.randomuser.core.ktx.calculateOffset
import io.github.joaogouveia89.randomuser.core.ktx.hadPassedOneMinute
import io.github.joaogouveia89.randomuser.core.ktx.humanizedHourMin
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserFetchState
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.randomUser.presentation.state.UserProfileState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

private const val CACHE_LIFETIME_MS = 5000L

@HiltViewModel
class RandomUserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private var chronJob: Job? = null
    private val locationTime = MutableStateFlow(Clock.System.now())
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
                        startChronometer(randomUserResponse.user.timezoneOffset)

                        UserProfileState(
                            isLoading = false,
                            user = randomUserResponse.user
                        )
                    }

                    is UserFetchState.Error -> {
                        UserProfileState()
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(stopTimeoutMillis = CACHE_LIFETIME_MS),
                initialValue = UserProfileState()
            )


    val uiState: StateFlow<UserProfileState> = combine(
        userProfileRequest,
        locationTime,
        refreshUser
    ) { profileRequest, locationTime, refreshedUser ->
        handleRefreshedUser(
            state = refreshedUser,
            profileRequest = profileRequest
        ).copy(locationTime = locationTime.humanizedHourMin())
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = UserProfileState()
    )

    private fun handleRefreshedUser(state: UserFetchState?, profileRequest: UserProfileState): UserProfileState =
        state?.let { refreshedState ->
            when (refreshedState) {
                is UserFetchState.Error -> profileRequest
                UserFetchState.Loading -> profileRequest.copy(
                    isGettingNewUser = true,
                    user = uiState.value.user
                )

                is UserFetchState.Success -> {
                    startChronometer(refreshedState.user.timezoneOffset)
                    profileRequest.copy(
                        isGettingNewUser = false,
                        user = refreshedState.user
                    )
                }
            }
        } ?:  profileRequest

    fun execute(command: RandomUserCommand) {
        when (command) {
            is RandomUserCommand.GetNewUser -> getNewUser()
            is RandomUserCommand.SaveUser -> saveUser()
        }
    }

    private fun startChronometer(offset: String) {
        stopChronometer()
        chronJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                var currentInst = Clock.System.now().calculateOffset(offset)
                locationTime.emit(currentInst)
                while (isActive) {
                    delay(1000)
                    currentInst = currentInst.plus(1.seconds)
                    if (currentInst.hadPassedOneMinute(locationTime.value)) {
                        locationTime.emit(currentInst)
                    }
                }
            }catch (exception: Exception){
                Log.e(TAG, "chronJob has stopped due to ${exception.message}")
            }
        }
    }

    private fun stopChronometer() {
        chronJob?.let { job ->
            viewModelScope.launch {
                /*
                 * Avoiding racing conditions, so it ensures the coroutine is
                 * finished before assigning another one to chronJob
                 */
                job.cancelAndJoin()
                chronJob = null
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

    private fun saveUser() {
        // TODO: saving is working it's missing to handle the saving status, disable save button if success or show error if a problem happens
        viewModelScope.launch {
            repository.saveUser(uiState.value.user).collect{

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopChronometer()
    }

    companion object{
        private val TAG = this::class.java.name
    }
}