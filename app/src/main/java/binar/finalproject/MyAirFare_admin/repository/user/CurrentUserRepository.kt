package binar.finalproject.MyAirFare_admin.repository.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.model.user.CurrentUser
import binar.finalproject.MyAirFare.api.AuthEndPoint
import binar.finalproject.MyAirFare.model.user.UserCurrentUpdate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CurrentUserRepository @Inject constructor(private val api : AuthEndPoint) {

    private val currentUser : MutableLiveData<CurrentUser?> = MutableLiveData()
    fun currentUserObserver() = currentUser

    private val currentUserUpdate : MutableLiveData<UserCurrentUpdate?> = MutableLiveData()
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

    fun currentUserUpdate(token : String,username : RequestBody,firstname : RequestBody,
    lastname : RequestBody,email : RequestBody,password : RequestBody,confirmPassword : RequestBody,
                          tittle : RequestBody,image : MultipartBody.Part){

        api.updateCurrentUser(token,username,firstname,lastname,email,password,confirmPassword,tittle,image)
            .enqueue(object : Callback<UserCurrentUpdate>{
                override fun onResponse(
                    call: Call<UserCurrentUpdate>,
                    response: Response<UserCurrentUpdate>
                ) {
                    if(response.isSuccessful){
                        val body = response.body()
                        Log.d("success body","$body")
                        if(body != null){
                            currentUserUpdate.postValue(body)
                            Log.d("success","$body")
                        }else{
                            currentUserUpdate.postValue(null)
                            message.postValue(response.message())
                            Log.d("Response Error Body","Response Error => ${response.code()}")
                        }
                    }else{
                        currentUserUpdate.postValue(null)
                        message.postValue(response.message())
                        Log.d("Response Error yg","Response Error => ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UserCurrentUpdate>, t: Throwable) {
                    currentUser.postValue(null)
                    message.postValue(t.message)
                    Log.d("Response Error Failure","Response Error => ${t.message}")
                }

            })

    }



}