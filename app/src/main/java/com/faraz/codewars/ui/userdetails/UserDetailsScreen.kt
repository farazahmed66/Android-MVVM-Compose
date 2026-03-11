package com.faraz.codewars.ui.userdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.faraz.codewars.R
import com.faraz.codewars.base.AppProgressBar
import com.faraz.codewars.models.UserChallengeData
import com.faraz.codewars.network.Resource
import com.faraz.codewars.ui.userdetails.authoredchallenge.AuthoredChallengeViewModel
import com.faraz.codewars.ui.userdetails.completedchallenge.CompletedChallengeViewModel

@Composable
fun UserDetailsScreen(
    completedViewModel: CompletedChallengeViewModel,
    authoredViewModel: AuthoredChallengeViewModel,
    onChallengeClick: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.completed_challenge) to Icons.Default.CheckCircle,
        stringResource(R.string.authored_challenge) to Icons.Default.Edit
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = colorResource(id = R.color.cardBackgroundColor)
            ) {
                tabs.forEachIndexed { index, (label, icon) ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        label = { Text(label) },
                        icon = { Icon(icon, contentDescription = null) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorResource(id = R.color.purple_500),
                            selectedTextColor = colorResource(id = R.color.purple_500),
                            unselectedIconColor = colorResource(id = R.color.secondaryTextColor),
                            unselectedTextColor = colorResource(id = R.color.secondaryTextColor),
                            indicatorColor = colorResource(id = R.color.contentContainerColor)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = colorResource(id = R.color.contentContainerColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) {
                if (selectedTab == 0) {
                    CompletedChallengesList(completedViewModel, onChallengeClick)
                } else {
                    AuthoredChallengesList(authoredViewModel, onChallengeClick)
                }
            }
        }
    }
}

@Composable
fun CompletedChallengesList(
    viewModel: CompletedChallengeViewModel,
    onChallengeClick: (String) -> Unit
) {
    val challenges: LazyPagingItems<UserChallengeData> =
        viewModel.completedChallenges.collectAsLazyPagingItems()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            count = challenges.itemCount,
            key = challenges.itemKey { it.id }
        ) { index ->
            challenges[index]?.let { challenge ->
                ChallengeItem(
                    name = challenge.name ?: "",
                    languages = challenge.completedLanguages.joinToString(", "),
                    onClick = { onChallengeClick(challenge.id) }
                )
            }
        }

        challenges.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { AppProgressBar() }
                }

                loadState.append is LoadState.Loading -> {
                    item { AppProgressBar() }
                }

                loadState.refresh is LoadState.Error -> {
                    val e = challenges.loadState.refresh as LoadState.Error
                    item { ErrorBox(e.error.localizedMessage ?: "Error", onRetry = { retry() }) }
                }

                loadState.append is LoadState.Error -> {
                    val e = challenges.loadState.append as LoadState.Error
                    item { ErrorBox(e.error.localizedMessage ?: "Error", onRetry = { retry() }) }
                }
            }
        }
    }
}

@Composable
fun AuthoredChallengesList(
    viewModel: AuthoredChallengeViewModel,
    onChallengeClick: (String) -> Unit
) {
    val state by viewModel.getAuthoredChallengeResponse.collectAsState()

    when (state) {
        is Resource.Loading -> AppProgressBar()
        is Resource.Success -> {
            val list = (state as Resource.Success).value.authoredChallengeData
            if (list.isEmpty()) {
                EmptyBox()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(list.size) { index ->
                        val challenge = list[index]
                        ChallengeItem(
                            name = challenge.name ?: "",
                            languages = challenge.languages?.joinToString(", ") ?: "",
                            onClick = { onChallengeClick(challenge.id ?: "") }
                        )
                    }
                }
            }
        }

        is Resource.Failure -> ErrorBox(
            message = stringResource(id = R.string.something_went_wrong),
            onRetry = { /* Retry logic */ }
        )

        else -> Unit
    }
}

@Composable
fun ChallengeItem(name: String, languages: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.cardBackgroundColor)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.primaryTextColor)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = languages,
                    style = MaterialTheme.typography.bodySmall,
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

@Composable
fun ErrorBox(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, color = colorResource(id = R.color.primaryTextColor))
        Button(onClick = onRetry) { Text(stringResource(R.string.retry)) }
    }
}

@Composable
fun EmptyBox() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.no_data),
            color = colorResource(id = R.color.secondaryTextColor)
        )
    }
}