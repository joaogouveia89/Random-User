package io.github.joaogouveia89.randomuser.core.presentation.screen.contentContainer.state

import androidx.annotation.StringRes

sealed class ContentState{
    data object Loading: ContentState()
    data object Offline: ContentState()
    data class Error(@StringRes val messageRes: Int): ContentState()
    data object Ready: ContentState()
}