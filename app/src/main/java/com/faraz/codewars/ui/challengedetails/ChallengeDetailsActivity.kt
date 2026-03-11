package com.faraz.codewars.ui.challengedetails

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faraz.codewars.R
import com.faraz.codewars.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChallengeDetailsActivity : BaseActivity() {

    private val viewModel: ChallengeDetailsViewModel by viewModels()
    private lateinit var mChallengeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mChallengeId = intent.getStringExtra("challenge_id") ?: ""

        setToolbarVisible(true)
        setToolbarTitle(getString(R.string.challenge_details))

        viewModel.getChallengeDetails(mChallengeId)

        setComposeContent {
            val challengeDetails by viewModel.getChallengeDataResponse.collectAsStateWithLifecycle()

            ChallengeDetailsScreen(
                challengeDetails = challengeDetails,
                onRetry = {
                    tryAgain()
                },
            )
        }
    }

    override fun tryAgain() {
        viewModel.getChallengeDetails(mChallengeId)
    }
}