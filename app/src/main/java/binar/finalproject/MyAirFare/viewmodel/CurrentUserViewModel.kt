package binar.finalproject.MyAirFare.viewmodel

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare.repository.CurrentUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CurrentUserViewModel @Inject constructor(
    private val currentUserRepository: CurrentUserRepository): ViewModel() {


        fun currentUser(token : String) = currentUserRepository.currentUser(token)
        fun currentUserObserver() = currentUserRepository.currentUserObserver()

        fun currentUserUpdate(token : String, username : RequestBody, firstname : RequestBody,
                              lastname : RequestBody, email : RequestBody, password : RequestBody,
                              confirmPassword : RequestBody,
                              tittle : RequestBody, image : MultipartBody.Part,visa_number : RequestBody
        ) = currentUserRepository.currentUserUpdate(token,username,firstname,lastname,email,
            password,confirmPassword,tittle,image,visa_number)

        fun currentUserUpdateObserver() = currentUserRepository.currentUserUpdateObserver()
        fun messageObserver() = currentUserRepository.messageObserver()
}