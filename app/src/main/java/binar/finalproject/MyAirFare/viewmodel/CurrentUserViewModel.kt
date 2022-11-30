package binar.finalproject.MyAirFare.viewmodel

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare.repository.CurrentUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrentUserViewModel @Inject constructor(
    private val currentUserRepository: CurrentUserRepository): ViewModel() {


        fun currentUser(token : String) = currentUserRepository.currentUser(token)
        fun currentUserObserver() = currentUserRepository.currentUserObserver()
}