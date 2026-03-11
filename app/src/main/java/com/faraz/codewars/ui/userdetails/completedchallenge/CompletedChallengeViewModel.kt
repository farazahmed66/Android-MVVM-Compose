package com.faraz.codewars.ui.userdetails.completedchallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.faraz.codewars.models.UserChallengeData
import com.faraz.codewars.repository.CompletedChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CompletedChallengeViewModel @Inject constructor(
    private val completedChallengeRepository: CompletedChallengeRepository
) : ViewModel() {

    private val usernameFlow = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    val completedChallenges = usernameFlow
        .filterNotNull()
        .flatMapLatest { username ->
            completedChallengeRepository.getCompletedChallenge(username)
        }
        .cachedIn(viewModelScope)

    fun setUserName(userName: String) {
        usernameFlow.value = userName
    }
}