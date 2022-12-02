package binar.finalproject.MyAirFare_admin.repository.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.model.login.UserLoginResponse
import binar.finalproject.MyAirFare.api.AuthEndPoint
import binar.finalproject.MyAirFare.model.login.LoginGoogle
import binar.finalproject.MyAirFare.model.login.UserLoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api : AuthEndPoint) {


    private val doLogin : MutableLiveData<UserLoginResponse?> = MutableLiveData()
    fun doLoginObserver():LiveData<UserLoginResponse?> = doLogin

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message

    fun doLogin(email : String,password:String){
        api.doUserLogin(UserLoginRequest(email,password)).enqueue(object : Callback<UserLoginResponse>{
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
               if(response.isSuccessful){
                   val body = response.body()
                   if(body != null){
                       doLogin.postValue(body)
                       Log.d("success","$body")
                   }else{
                       doLogin.postValue(null)
                       message.postValue(response.message())
                       Log.d("Response Error","Response Error => ${response.message()}")
                       Log.d("Response Body Pertama","Response Error => ${response.body()}")
                   }
               }else{
                   doLogin.postValue(null)
                   message.postValue(response.message())
                   Log.d("Response Errorr","Response Error => ${response.code()}")
                   Log.d("Response Body Kedua","Response Error => ${response.body()}")
               }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                doLogin.postValue(null)
                message.postValue(t.message)
                Log.d("Response Errorrr","Response Error => ${t.message}")
            }

        })
    }



}