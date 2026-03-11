package com.faraz.codewars.ui.userdetails.authoredchallenge

import com.faraz.codewars.models.AuthoredChallengeData

interface AuthoredAdapterClickListener {
    fun itemClicked(data: AuthoredChallengeData)
}