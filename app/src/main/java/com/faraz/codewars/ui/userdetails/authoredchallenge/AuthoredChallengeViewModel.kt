package com.faraz.codewars.ui.userdetails.authoredchallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faraz.codewars.models.AuthoredChallenge
import com.faraz.codewars.network.Resource
import com.faraz.codewars.repository.AuthoredChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthoredChallengeViewModel @Inject constructor(
    private val authoredChallengeRepository: AuthoredChallengeRepository
) : ViewModel() {

    private val _authoredChallengeResponse =
        MutableStateFlow<Resource<AuthoredChallenge>>(Resource.Empty)
    val getAuthoredChallengeResponse: StateFlow<Resource<AuthoredChallenge>> =
        _authoredChallengeResponse

    fun getAuthoredChallenge(user: String) {
        viewModelScope.launch {
            authoredChallengeRepository
                .getAuthoredChallenge(user)
                .collect {
                    _authoredChallengeResponse.value = it
                }
        }
    }

}