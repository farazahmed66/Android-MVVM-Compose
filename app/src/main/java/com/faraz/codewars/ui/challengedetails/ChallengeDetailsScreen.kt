package com.faraz.codewars.ui.challengedetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faraz.codewars.R
import com.faraz.codewars.base.AppProgressBar
import com.faraz.codewars.models.ChallengeDetails
import com.faraz.codewars.network.Resource
import com.faraz.codewars.utils.toReadableDate

@Composable
fun ChallengeDetailsScreen(
    challengeDetails: Resource<ChallengeDetails>,
    onRetry: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.contentContainerColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
                .padding(10.dp)
        ) {
            when (challengeDetails) {
                is Resource.Success -> {
                    val challenge = challengeDetails.value

                    SetInfoItem(
                        label = stringResource(id = R.string.name),
                        value = challenge.name ?: ""
                    )
                    SetInfoItem(
                        label = stringResource(id = R.string.desc),
                        value = challenge.description ?: ""
                    )
                    SetInfoItem(
                        label = stringResource(id = R.string.category),
                        value = challenge.category ?: ""
                    )
                    SetInfoItem(
                        label = stringResource(id = R.string.languages),
                        value = challenge.languages?.joinToString(", ") ?: ""
                    )
                    SetInfoItem(
                        label = stringResource(id = R.string.created_by),
                        value = challenge.createdBy?.username ?: ""
                    )
                    SetInfoItem(
                        label = stringResource(id = R.string.created_at),
                        value = challenge.createdAt.toReadableDate()
                    )
                    SetInfoItem(
                        label = stringResource(id = R.string.approved_by),
                        value = challenge.approvedBy?.username ?: ""
                    )
                    SetInfoItem(
                        label = stringResource(id = R.string.approved_at),
                        value = challenge.approvedAt.toReadableDate()
                    )
                }

                is Resource.Failure -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = if (challengeDetails.isNetworkError)
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

                is Resource.Loading -> AppProgressBar()

                Resource.Empty -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Getting details",
                        color = colorResource(id = R.color.secondaryTextColor)
                    )
                }
            }
        }
    }

}

@Composable
fun SetInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(0.3f),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = colorResource(id = R.color.primaryTextColor)
        )
        Text(
            text = value,
            modifier = Modifier.weight((0.7f)),
            fontSize = 12.sp,
            color = colorResource(id = R.color.primaryTextColor)
        )
    }
}