package com.faraz.codewars.ui.users

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faraz.codewars.base.BaseActivity
import com.faraz.codewars.models.User
import com.faraz.codewars.ui.userdetails.UserDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListActivity : BaseActivity() {

    private val viewModel: UserListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarVisible(false)

        setComposeContent {
            val userState by viewModel.getUserDataResponse.collectAsStateWithLifecycle()

            UserListScreen(
                userState = userState,
                onSearch = { query ->
                    viewModel.searchUser(query)
                },
                onUserClick = { user ->
                    navigateToDetails(user)
                },
                onRetry = {
                    tryAgain()
                }
            )
        }
    }

    private fun navigateToDetails(user: User) {
        val intent = Intent(this, UserDetailsActivity::class.java)
        intent.putExtra("userName", user.userName)
        startActivity(intent)
    }

    override fun tryAgain() {
        viewModel.retry()
    }
}