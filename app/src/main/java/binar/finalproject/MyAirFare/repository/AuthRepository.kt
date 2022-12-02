package binar.finalproject.MyAirFare.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.model.login.UserLoginResponse
import binar.finalproject.MyAirFare.model.register.UserRegisterRequest
import binar.finalproject.MyAirFare.model.register.UserRegisterResponse
import binar.finalproject.MyAirFare.api.AuthEndPoint
import binar.finalproject.MyAirFare.model.login.LoginGoogle
import binar.finalproject.MyAirFare.model.login.UserLoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api : AuthEndPoint) {

    private val doRegister : MutableLiveData<UserRegisterResponse?> = MutableLiveData()
    fun doRegisterObserver():LiveData<UserRegisterResponse?> = doRegister

    private val doLogin : MutableLiveData<UserLoginResponse?> = MutableLiveData()
    fun doLoginObserver():LiveData<UserLoginResponse?> = doLogin

    private val doLoginWithGoogle : MutableLiveData<UserRegisterResponse?> = MutableLiveData()
    fun doLoginWithGoogleObserver():LiveData<UserRegisterResponse?> = doLoginWithGoogle

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message



   fun doRegister(
        username :String,l_name : String,
        f_name : String,email : String,
        password : String,confirm_password : String,title :String){
        api.doUserRegister(UserRegisterRequest(
            username,f_name,l_name,email,password,confirm_password,title
        )).enqueue(object : Callback<UserRegisterResponse>{
            override fun onResponse(
                call: Call<UserRegisterResponse>,
                response: Response<UserRegisterResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doRegister.postValue(body)
                        Log.d("Success","$body")
                    }else{
                        doRegister.postValue(null)
                        message.postValue(response.message())
                        Log.d("Response Error","Response Error => ${response.message()}")
                    }
                }else{
                    doRegister.postValue(null)
                    message.postValue(response.message())
                    Log.d("Response Error ygy","Response Error => ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                doRegister.postValue(null)
                message.postValue(t.message)
                Log.d("Response Error","Response Error => ${t.message}")
            }

        })
    }

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

    fun doLoginWithGoogle(credential : String){
        api.doLoginWithGoole(LoginGoogle(credential)).enqueue(object : Callback<UserRegisterResponse>{
            override fun onResponse(
                call: Call<UserRegisterResponse>,
                response: Response<UserRegisterResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doLoginWithGoogle.postValue(body)
                        Log.d("Success","$body")
                    }else{
                        message.postValue(response.message())
                        doLoginWithGoogle.postValue(null)
                        Log.d("Response Error","Response Error => ${response.message()}")
                    }
                }else{
                    doLoginWithGoogle.postValue(null)
                    message.postValue(response.body().toString())
                    Log.d("Response Error ygy","Response Error => ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                message.postValue(t.message)
                doLoginWithGoogle.postValue(null)
                Log.d("Response Error","Response Error => ${t.message}")
            }

        })
    }

}