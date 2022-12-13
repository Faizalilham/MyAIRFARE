package binar.finalproject.MyAirFare.repository.waitlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.ApiEndPoint
import binar.finalproject.MyAirFare.model.wait_list.*
import binar.finalproject.MyAirFare.utils.ErrorValidation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WaitListRepository @Inject constructor(private val api : ApiEndPoint) {

    private val getAllWaitList : MutableLiveData<MutableList<WaitList>?> = MutableLiveData()
    fun getAllWaitListObserver() : LiveData<MutableList<WaitList>?> = getAllWaitList

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message

    fun getAllWaitList(token : String){
        api.getAllListCart(token).enqueue(object : Callback<WaitListResponse>{
            override fun onResponse(
                call: Call<WaitListResponse>,
                response: Response<WaitListResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        getAllWaitList.postValue(body.wait_list)
                        Log.d("SUCCESS","$body")
                    }else{
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        getAllWaitList.postValue(null)
                        message.postValue(error)
                        Log.d("ERROR",response.code().toString())
                    }
                }else{
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    getAllWaitList.postValue(null)
                    message.postValue(error)
                    Log.d("ERROR",response.code().toString())
                }
            }

            override fun onFailure(call: Call<WaitListResponse>, t: Throwable) {
                getAllWaitList.postValue(null)
                message.postValue(t.message)
                Log.d("ERROR",t.message.toString())
            }

        })
    }

    private val postWaitList : MutableLiveData<WaitList?> = MutableLiveData()
    fun postWaitListObserver() : LiveData<WaitList?> = postWaitList

    fun postWaitList(token : String, tickets_id : String){
        api.postCart(token, WaitListRequest(mutableListOf(tickets_id)))
            .enqueue(object : Callback<WaitList>{
                override fun onResponse(call: Call<WaitList>, response: Response<WaitList>) {

                    if(response.isSuccessful){
                        val body = response.body()
                        if(body != null){
                            postWaitList.postValue(body)
                            Log.d("SUCCESS","$body")
                        }else{
                            val error = ErrorValidation.errorAuthValidation(response.code())
                            postWaitList.postValue(null)
                            message.postValue(error)
                            Log.d("ERROR",response.code().toString())
                        }
                    }else{
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        postWaitList.postValue(null)
                        message.postValue(error)
                        Log.d("ERROR",response.code().toString())
                    }

                }

                override fun onFailure(call: Call<WaitList>, t: Throwable) {
                    postWaitList.postValue(null)
                    message.postValue(t.message)
                    Log.d("ERROR",t.message.toString())
                }

            })

    }

    private val deleteWaitList : MutableLiveData<WaitListDeleteResponse?> = MutableLiveData()
    fun deleteWaitListObserver() : LiveData<WaitListDeleteResponse?> = deleteWaitList


    fun deleteWaitList(token : String,id : Int){
        api.deleteCart(token,id).enqueue(object : Callback<WaitListDeleteResponse>{
            override fun onResponse(
                call: Call<WaitListDeleteResponse>,
                response: Response<WaitListDeleteResponse>
            ) {

                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        deleteWaitList.postValue(body)
                        Log.d("SUCCESS","$body")
                    }else{
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        deleteWaitList.postValue(null)
                        message.postValue(error)
                        Log.d("ERROR",response.code().toString())
                    }
                }else{
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    deleteWaitList.postValue(null)
                    message.postValue(error)
                    Log.d("ERROR",response.code().toString())
                }


            }

            override fun onFailure(call: Call<WaitListDeleteResponse>, t: Throwable) {
                deleteWaitList.postValue(null)
                message.postValue(t.message)
                Log.d("ERROR",t.message.toString())
            }

        })
    }




}