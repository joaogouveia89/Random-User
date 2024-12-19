package io.github.joaogouveia89.randomuser.data.repository

import io.github.joaogouveia89.randomuser.domain.repository.UserFetchState
import io.github.joaogouveia89.randomuser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.domain.source.UserSource
import io.github.joaogouveia89.randomuser.remoteService.model.mappers.asUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl(
    private val source: UserSource
) : UserRepository {
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