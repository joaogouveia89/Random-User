package io.github.joaogouveia89.randomuser.userDetail.data.repository

import android.util.Log
import io.github.joaogouveia89.randomuser.core.di.IoDispatcher
import io.github.joaogouveia89.randomuser.randomUser.data.repository.UserRepositoryImpl
import io.github.joaogouveia89.randomuser.userDetail.domain.repository.UserDetailDeleteState
import io.github.joaogouveia89.randomuser.userDetail.domain.repository.UserDetailGetState
import io.github.joaogouveia89.randomuser.userDetail.domain.repository.UserDetailRepository
import io.github.joaogouveia89.randomuser.userDetail.domain.source.UserDetailSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UserDetailRepositoryImpl @Inject constructor(
    private val userDetailSource: UserDetailSource,
    @IoDispatcher private val dispatcher: CoroutineContext
) : UserDetailRepository {
    override suspend fun getUser(userId: Long): Flow<UserDetailGetState> = flow {
        emit(UserDetailGetState.Loading)
        val user = userDetailSource.getUser(userId)
        emit(UserDetailGetState.Success(user = user))
    }.flowOn(dispatcher)

    override suspend fun deleteUser(id: Long): Flow<UserDetailDeleteState> = flow {
        emit(UserDetailDeleteState.Loading)
        try {
            userDetailSource.deleteUser(id)
            emit(UserDetailDeleteState.Success)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "")
            emit(UserDetailDeleteState.Error)
        }
    }

    companion object {
        val TAG = UserRepositoryImpl::class.java.name
    }
}