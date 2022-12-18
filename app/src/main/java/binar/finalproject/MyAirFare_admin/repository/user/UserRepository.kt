package binar.finalproject.MyAirFare_admin.repository.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.AuthEndPoint
import binar.finalproject.MyAirFare_admin.model.user.*
import binar.finalproject.MyAirFare_admin.utils.ErrorValidation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val api : AuthEndPoint) {

    private val  getAllUser : MutableLiveData<MutableList<User>?> = MutableLiveData()
    fun getAllUserObserver() :LiveData<MutableList<User>?> = getAllUser

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message

    fun getAllUser(token : String){
        api.getAllUser(token).enqueue(object : Callback<ListUserResponse>{
            override fun onResponse(
                call: Call<ListUserResponse>,
                response: Response<ListUserResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    Log.d("success body","$body")
                    if(body != null){
                        getAllUser.postValue(body.users)
                        Log.d("success","$body")
                    }else{
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        getAllUser.postValue(null)
                        message.postValue(error)
                        Log.d("ERROR",response.code().toString())
                    }
                }else{
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    getAllUser.postValue(null)
                    message.postValue(error)
                    Log.d("Response Error yg","Response Error => ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ListUserResponse>, t: Throwable) {
                getAllUser.postValue(null)
                message.postValue(t.message.toString())
            }

        })
    }

    private val getUserById : MutableLiveData<SingleUserResponse?> = MutableLiveData()
    fun getUserByIdObserver() :LiveData<SingleUserResponse?> = getUserById


    fun getUserById(token : String,id : String){
        api.getUserById(token,id).enqueue(object : Callback<SingleUserResponse>{
            override fun onResponse(
                call: Call<SingleUserResponse>,
                response: Response<SingleUserResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    Log.d("success body","$body")
                    if(body != null){
                        getUserById.postValue(body)
                        Log.d("success","$body")
                    }else{
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        getUserById.postValue(null)
                        message.postValue(error)
                        Log.d("ERROR",response.code().toString())
                    }
                }else{
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    getUserById.postValue(null)
                    message.postValue(error)
                    Log.d("Response Error yg","Response Error => ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SingleUserResponse>, t: Throwable) {
                getUserById.postValue(null)
                message.postValue(t.message.toString())
            }

        })
    }

    private val filterUser : MutableLiveData<MutableList<UserEmail>?> = MutableLiveData()
    fun filterUserObserver() :LiveData<MutableList<UserEmail>?> = filterUser

    fun filterUser(token :String,email : String){
        api.filterUser(token,email).enqueue(object : Callback<ListEmailResponse>{
            override fun onResponse(
                call: Call<ListEmailResponse>,
                response: Response<ListEmailResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    Log.d("success body","$body")
                    if(body != null){
                        filterUser.postValue(body.user)
                        Log.d("success","$body")
                    }else{
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        filterUser.postValue(null)
                        message.postValue(error)
                        Log.d("ERROR",response.code().toString())
                    }
                }else{
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    filterUser.postValue(null)
                    message.postValue(error)
                    Log.d("Response Error yg","Response Error => ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ListEmailResponse>, t: Throwable) {
                filterUser.postValue(null)
                message.postValue(t.message.toString())
            }

        })
    }

    private val doDeleteUser : MutableLiveData<DeleteUserResponse?> = MutableLiveData()
    fun doDeleteUserObserver():LiveData<DeleteUserResponse?> = doDeleteUser


    fun doDeleteUser(token : String,id : String){
        api.doDeleteUser(token,id).enqueue(object : Callback<DeleteUserResponse>{
            override fun onResponse(
                call: Call<DeleteUserResponse>,
                response: Response<DeleteUserResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    Log.d("success body","$body")
                    if(body != null){
                        doDeleteUser.postValue(body)
                        Log.d("success","$body")
                    }else{
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        doDeleteUser.postValue(null)
                        message.postValue(error)
                        Log.d("ERROR",response.code().toString())
                    }
                }else{
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    doDeleteUser.postValue(null)
                    message.postValue(error)
                    Log.d("Response Error yg","Response Error => ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DeleteUserResponse>, t: Throwable) {
                doDeleteUser.postValue(null)
                message.postValue(t.message.toString())
            }

        })
    }




}