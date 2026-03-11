package com.faraz.codewars.ui.userdetails

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.faraz.codewars.base.BaseActivity
import com.faraz.codewars.ui.challengedetails.ChallengeDetailsActivity
import com.faraz.codewars.ui.userdetails.authoredchallenge.AuthoredChallengeViewModel
import com.faraz.codewars.ui.userdetails.completedchallenge.CompletedChallengeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsActivity : BaseActivity() {

    private val viewModel: UserDetailsViewModel by viewModels()
    private val completedViewModel: CompletedChallengeViewModel by viewModels()
    private val authoredViewModel: AuthoredChallengeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userName = intent.getStringExtra("userName") ?: ""
        
        setToolbarVisible(true)
        setToolbarTitle(userName)

        completedViewModel.setUserName(userName)
        authoredViewModel.getAuthoredChallenge(userName)

        setComposeContent {
            UserDetailsScreen(
                completedViewModel = completedViewModel,
                authoredViewModel = authoredViewModel,
                onChallengeClick = { challengeId ->
                    val intent = Intent(this, ChallengeDetailsActivity::class.java)
                    intent.putExtra("challenge_id", challengeId)
                    startActivity(intent)
                }
            )
        }

        deleteUserData()
    }

    private fun deleteUserData() {
        viewModel.deleteUser()
    }

    override fun tryAgain() {
    }
}