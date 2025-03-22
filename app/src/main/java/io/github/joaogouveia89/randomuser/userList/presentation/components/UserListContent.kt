package io.github.joaogouveia89.randomuser.userList.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joaogouveia89.randomuser.core.fakeData.fakeUser
import io.github.joaogouveia89.randomuser.core.model.User
import io.github.joaogouveia89.randomuser.userList.presentation.state.UserListState

@Composable
fun UserListContent(
    uiState: UserListState,
    onSearchQueryChange: (String) -> Unit,
    onUserClick: (User) -> Unit,
    onUserLongClick: (User) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // User List or Empty State
        if (uiState.hasNoUser) {
            UserListEmpty()
        } else {
            if(uiState.isMultiSelectionMode){
                
            }else{
                SearchBar(
                    onSearchQueryChange = onSearchQueryChange
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.userList) { user ->
                    UserListItem(
                        user = user.first,
                        isSelected = user.second,
                        onClick = { onUserClick(user.first) },
                        onLongClick = { onUserLongClick(user.first) },
                    )
                    Spacer(modifier = Modifier.height(12.dp)) // Add spacing between items
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserListContentPreview() {
    UserListContent(
        uiState = UserListState(
            userList = listOf(Pair(fakeUser, false), Pair(fakeUser, false))
        ),
        onSearchQueryChange = {},
        onUserClick = {},
        onUserLongClick = {},
    )
}