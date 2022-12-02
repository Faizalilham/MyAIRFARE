package binar.finalproject.MyAirFare_admin.viewmodel.auth

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare_admin.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository : AuthRepository):ViewModel() {

    fun doLogin(email : String,password : String) = authRepository.doLogin(email,password)

    fun messageObserver() = authRepository.messageObserver()

}