package com.faraz.codewars.ui.challengedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faraz.codewars.models.ChallengeDetails
import com.faraz.codewars.network.Resource
import com.faraz.codewars.repository.ChallengeDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeDetailsViewModel @Inject constructor(
    private val challengeDetailRepository: ChallengeDetailRepository
) : ViewModel() {

    private val _challengeDetailsResponse = MutableStateFlow<Resource<ChallengeDetails>>(Resource.Empty)
    val getChallengeDataResponse: StateFlow<Resource<ChallengeDetails>> = _challengeDetailsResponse

    fun getChallengeDetails(challengeId: String) {
        viewModelScope.launch {
            challengeDetailRepository
                .getChallengeDetails(challengeId)
                .collect {
                    _challengeDetailsResponse.value = it
                }
        }
    }
}