package com.faraz.codewars.ui.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faraz.codewars.R
import com.faraz.codewars.models.User
import com.faraz.codewars.network.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    userState: Resource<User>,
    onSearch: (String) -> Unit,
    onUserClick: (User) -> Unit,
    onRetry: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.contentContainerColor)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val onActiveChange = { it: Boolean -> active = it }
            val colors1 = SearchBarDefaults.colors()

            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = {
                            onSearch(it)
                            active = false
                            focusManager.clearFocus()
                        },
                        expanded = active,
                        onExpandedChange = onActiveChange,
                        enabled = true,
                        placeholder = { 
                            Text(
                                "Search...", 
                                color = colorResource(id = R.color.secondaryTextColor)
                            ) 
                        },
                        leadingIcon = { 
                            Icon(
                                Icons.Default.Search, 
                                contentDescription = null,
                                tint = colorResource(id = R.color.iconColor)
                            ) 
                        },
                        trailingIcon = null,
                        colors = colors1.inputFieldColors,
                    )
                },
                expanded = active,
                onExpandedChange = onActiveChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (active) 0.dp else 16.dp),
                content = {
                    // Can add search suggestions here if needed
                },
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (userState) {
                    is Resource.Loading -> CircularProgressIndicator(color = colorResource(id = R.color.purple_500))
                    is Resource.Success -> {
                        UserCard(user = userState.value, onClick = { onUserClick(userState.value) })
                    }
                    is Resource.Failure -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = if (userState.isNetworkError)
                                    stringResource(R.string.no_internet)
                                else
                                    stringResource(R.string.something_went_wrong),
                                color = colorResource(id = R.color.primaryTextColor),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Button(
                                onClick = onRetry,
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_500))
                            ) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    }
                    Resource.Empty -> {
                        Text(
                            text = "Search for a user to see details",
                            color = colorResource(id = R.color.secondaryTextColor)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.cardBackgroundColor)),
        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${stringResource(R.string.name)}${user.name ?: ""}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.primaryTextColor)
                )
                Text(
                    text = "Honor: ${user.honor ?: ""}", 
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.secondaryTextColor)
                )
                Text(
                    text = "Clan: ${user.clan ?: ""}", 
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.secondaryTextColor)
                )
                Text(
                    text = "Position: ${user.leaderboardPosition ?: ""}", 
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.secondaryTextColor)
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight, 
                contentDescription = null,
                tint = colorResource(id = R.color.iconColor)
            )
        }
    }
}