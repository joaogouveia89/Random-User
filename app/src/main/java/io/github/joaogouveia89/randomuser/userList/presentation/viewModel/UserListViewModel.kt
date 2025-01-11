package io.github.joaogouveia89.randomuser.userList.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joaogouveia89.randomuser.core.di.IoDispatcher
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListGetState
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: UserListRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {
    init {
        viewModelScope.launch(dispatcher) {
            repository.getUsers().collect{
                if(it is UserListGetState.Success){
                    Log.i("JOAODEBUG", it.users.toString())
                }
            }
        }
    }
}