package io.github.joaogouveia89.randomuser.userList.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.joaogouveia89.randomuser.R
import io.github.joaogouveia89.randomuser.core.presentation.components.useDebounce

// Thanks to https://mohammedev.hashnode.dev/usedebounce-in-androids-jetpack-compose
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = stringResource(R.string.search),
    queryDebounceMs: Long = 300,
    onSearchQueryChange: (String) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }

    query.useDebounce(
        onChange = onSearchQueryChange,
        delayMillis = queryDebounceMs
    )

    var isHintDisplayed by remember {
        mutableStateOf(hint.isNotEmpty())
    }

    Box(modifier = modifier) {
        TextField(
            value = query,
            onValueChange = {
                query = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .onFocusChanged {
                    // This will show the hint when unfocused
                    isHintDisplayed = !it.isFocused
                },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            placeholder = {
                Text(text = "Search users...")
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                fontSize = 12.sp
            )
        }
    }


}