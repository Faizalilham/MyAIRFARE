package binar.finalproject.MyAirFare_admin.repository.transactions

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.ApiEndPoint
import binar.finalproject.MyAirFare_admin.model.transactions.ReadTransactionsResponse
import binar.finalproject.MyAirFare_admin.utils.ErrorValidation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class TransactionsRepository @Inject constructor(private val api : ApiEndPoint) {

    private val doGetAllTransactions : MutableLiveData<ReadTransactionsResponse?> = MutableLiveData()
    fun getAllTransactionsObserver() : LiveData<ReadTransactionsResponse?> = doGetAllTransactions

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message

    fun doGetAllTransactions(token : String){
        api.getAllTransactions(token).enqueue(object : Callback<ReadTransactionsResponse> {
            override fun onResponse(
                call: Call<ReadTransactionsResponse>,
                response: Response<ReadTransactionsResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doGetAllTransactions.postValue(body)
                        Log.d("success","$body")
                    }else{
                        doGetAllTransactions.postValue(null)
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        message.postValue(error)
                        Log.d("errorr","${response.code()}")
                    }
                }else{
                    doGetAllTransactions.postValue(null)
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    message.postValue(error)
                    Log.d("errorr","${response.code()}")
                }
            }

            override fun onFailure(call: Call<ReadTransactionsResponse>, t: Throwable) {
                doGetAllTransactions.postValue(null)
                Log.d("error","${t.message}")
            }

        })
    }
}