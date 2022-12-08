package binar.finalproject.MyAirFare_admin.viewmodel.user

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare_admin.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){

    fun getAllUser(token : String) = userRepository.getAllUser(token)
    fun getAllUserObserver() = userRepository.getAllUserObserver()

    fun getUserById(token : String,id : String) = userRepository.getUserById(token,id)
    fun getUserByIdObserver() = userRepository.getUserByIdObserver()

    fun filterUser(token : String,email : String) = userRepository.filterUser(token,email)
    fun filterUserObserver() = userRepository.filterUserObserver()

    fun messageObserver() = userRepository.messageObserver()
}