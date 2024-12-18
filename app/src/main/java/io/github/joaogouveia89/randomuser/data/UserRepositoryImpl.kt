package io.github.joaogouveia89.randomuser.data

import io.github.joaogouveia89.randomuser.domain.UserFetchState
import io.github.joaogouveia89.randomuser.domain.UserRepository
import io.github.joaogouveia89.randomuser.domain.UserSource
import io.github.joaogouveia89.randomuser.remoteService.model.mappers.asUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl(
    private val source: UserSource
): UserRepository {
    override fun getRandomUser(): Flow<UserFetchState> = flow {
        emit(UserFetchState.Loading)

        val user = source
            .getRandomUser()
            .results
            .first()
            .asUser()

        emit(UserFetchState.Success(user))
    }.flowOn(Dispatchers.IO)
}