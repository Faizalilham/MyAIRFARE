package binar.finalproject.MyAirFare.viewmodel

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare.repository.ResetPasswordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordRepository: ResetPasswordRepository):ViewModel() {

        fun doResetPassword(email : String) = resetPasswordRepository.doResetPassword(email)
        fun doResetPasswordObserver() = resetPasswordRepository.doResetPasswordObserver()

}