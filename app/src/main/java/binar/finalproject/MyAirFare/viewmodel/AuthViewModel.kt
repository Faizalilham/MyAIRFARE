package binar.finalproject.MyAirFare.viewmodel

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository : AuthRepository):ViewModel() {

    fun doLogin(email : String,password : String) = authRepository.doLogin(email,password)
    fun doRegister(username :String,l_name : String,
                   f_name : String,email : String,
                   password : String,confirm_password : String,
                   title :String) = authRepository.doRegister(username,f_name,l_name,email,password,
                                                              confirm_password, title)

    fun doLoginObserver() = authRepository.doLoginObserver()
    fun doRegisterObserver() = authRepository.doRegisterObserver()

}