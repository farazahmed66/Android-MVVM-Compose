package com.faraz.codewars.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faraz.codewars.models.User
import com.faraz.codewars.network.Resource
import com.faraz.codewars.repository.UserListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userListRepository: UserListRepository
) : ViewModel() {

    private val _userListResponse = MutableStateFlow<Resource<User>>(Resource.Empty)
    val getUserDataResponse: StateFlow<Resource<User>> = _userListResponse

    private var lastQuery: String? = null

    fun searchUser(query: String) {
        lastQuery = query
        viewModelScope.launch {
            userListRepository.searchUser(query)
                .collect {
                    _userListResponse.value = it
                }
        }
    }

    fun retry() {
        lastQuery?.let { searchUser(it) }
    }
}