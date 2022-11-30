package binar.finalproject.MyAirFare.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.model.user.CurrentUser
import binar.finalproject.MyAirFare.api.AuthEndPoint
import binar.finalproject.MyAirFare.model.user.UserCurrentUpdate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CurrentUserRepository @Inject constructor(private val api : AuthEndPoint) {

    private val currentUser : MutableLiveData<CurrentUser?> = MutableLiveData()
    fun currentUserObserver() = currentUser

    private val currentUserUpdate : MutableLiveData<UserCurrentUpdate> = MutableLiveData()
    fun currentUserUpdateObserver() = currentUserUpdate

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message

    fun currentUser(token : String){
        api.getCurrentUser(token).enqueue(object : Callback<CurrentUser>{
            override fun onResponse(call: Call<CurrentUser>, response: Response<CurrentUser>) {

                if(response.isSuccessful){
                    val body = response.body()
                    Log.d("success body","$body")
                    if(body != null){
                        currentUser.postValue(body)
                        Log.d("success","$body")
                    }else{
                        currentUser.postValue(null)
                        message.postValue(response.message())
                        Log.d("Response Error Body","Response Error => ${response.code()}")
                    }
                }else{
                    currentUser.postValue(null)
                    message.postValue(response.message())
                    Log.d("Response Error yg","Response Error => ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CurrentUser>, t: Throwable) {
                currentUser.postValue(null)
                message.postValue(t.message)
                Log.d("Response Error Failure","Response Error => ${t.message}")
            }

        })
    }

    fun currentUserUpdate(){

    }



}