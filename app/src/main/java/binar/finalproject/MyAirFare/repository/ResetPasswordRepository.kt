package binar.finalproject.MyAirFare.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.model.reset_password.ResetPassword
import binar.finalproject.MyAirFare.api.AuthEndPoint
import binar.finalproject.MyAirFare.model.reset_password.ForgetPassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ResetPasswordRepository @Inject constructor(private val api : AuthEndPoint) {

    private val doResetPassword : MutableLiveData<ResetPassword?> = MutableLiveData()
    fun doResetPasswordObserver() = doResetPassword

    fun doResetPassword(email : String){
        api.doResetPassword(ForgetPassword(email)).enqueue(object : Callback<ResetPassword>{
            override fun onResponse(call: Call<ResetPassword>, response: Response<ResetPassword>) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doResetPassword.postValue(body)
                    }else{
                        doResetPassword.postValue(null)
                        Log.d("Response Error","Response Error => ${response.code()}")
                    }
                }else{
                    doResetPassword.postValue(null)
                    Log.d("Response Error","Response Error => ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResetPassword>, t: Throwable) {
                doResetPassword.postValue(null)
                Log.d("Response Error","Response Error => ${t.message}")
            }

        })
    }

}