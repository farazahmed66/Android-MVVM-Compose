package com.faraz.codewars.ui.userdetails.completedchallenge

import com.faraz.codewars.models.UserChallengeData

interface CompletedAdapterClickListener {
    fun itemClicked(data: UserChallengeData)
}