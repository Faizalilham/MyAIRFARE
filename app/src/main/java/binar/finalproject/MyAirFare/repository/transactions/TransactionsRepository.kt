package binar.finalproject.MyAirFare.repository.transactions

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.ApiEndPoint
import binar.finalproject.MyAirFare.model.transactions.*
import binar.finalproject.MyAirFare.utils.ErrorValidation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class TransactionsRepository @Inject constructor(private  val api : ApiEndPoint) {

    private val doPostTransactions : MutableLiveData<PostTransactionsResponse?> = MutableLiveData()
    fun postTransactionsObserver() : LiveData<PostTransactionsResponse?> = doPostTransactions

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message

    fun doPostTransactions(
        token : String,
        ticketId : MutableList<String>,chairs : MutableList<Int>){
        api.postTransaction(token, PostTransactionsRequest(ticketId,chairs))
            .enqueue(object  : Callback<PostTransactionsResponse>{
                override fun onResponse(
                    call: Call<PostTransactionsResponse>,
                    response: Response<PostTransactionsResponse>
                ) {
                    if(response.isSuccessful){
                        val body = response.body()
                        if(body != null){
                            doPostTransactions.postValue(body)
                            Log.d("success","$body")
                        }else{
                            doPostTransactions.postValue(null)
                            val error = ErrorValidation.errorAuthValidation(response.code())
                            message.postValue(error)
                            Log.d("errorr","${response.code()}")
                        }
                    }else{
                        doPostTransactions.postValue(null)
                        if(response.code() == 403){
                            message.postValue("Tidak bisa melakukan transaksi tiket dengan jam penerbangan yang sama")
                        }else if(response.code() == 401 || response.code() == 404){
                            message.postValue("Tidak bisa melakukan transaksi tiket yang sudah terlewat")
                        }else{
                            val error = ErrorValidation.errorAuthValidation(response.code())
                            message.postValue(error)
                        }
                        Log.d("errorr","${response.code()}")
                    }
                }

                override fun onFailure(call: Call<PostTransactionsResponse>, t: Throwable) {
                    doPostTransactions.postValue(null)
                    Log.d("error","${t.message}")
                }

            })
    }

    private val doGetAllTransactions : MutableLiveData<ReadTransactionsResponse?> = MutableLiveData()
    fun getAllTransactionsObserver() : LiveData<ReadTransactionsResponse?> = doGetAllTransactions

    fun doGetAllTransactions(token : String){
        api.getAllTransactions(token).enqueue(object : Callback<ReadTransactionsResponse>{
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

    private val doGetTransactionById : MutableLiveData<ReadTransactionsResponse?> = MutableLiveData()
    fun getTransactionByIdObserver() : LiveData<ReadTransactionsResponse?> = doGetTransactionById


    fun doGetTransactionById(token : String,id : Int){
        api.getTransactionById(token,id).enqueue(object : Callback<ReadTransactionsResponse>{
            override fun onResponse(
                call: Call<ReadTransactionsResponse>,
                response: Response<ReadTransactionsResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doGetTransactionById.postValue(body)
                        Log.d("success","$body")
                    }else{
                        doGetTransactionById.postValue(null)
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        message.postValue(error)
                        Log.d("errorr","${response.code()}")
                    }
                }else{
                    doGetTransactionById.postValue(null)
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    message.postValue(error)
                    Log.d("errorr","${response.code()}")
                }
            }

            override fun onFailure(call: Call<ReadTransactionsResponse>, t: Throwable) {
                doGetTransactionById.postValue(null)
                Log.d("error","${t.message}")
            }

        })
    }

    private val doDeleteTransaction : MutableLiveData<TransactionsDeleteResponse?> = MutableLiveData()
    fun deleteTransactionObserver() : LiveData<TransactionsDeleteResponse?> = doDeleteTransaction

    fun doDeleteTransaction(token : String,id : String){
        api.deleteTransaction(token,id).enqueue(object : Callback<TransactionsDeleteResponse>{
            override fun onResponse(
                call: Call<TransactionsDeleteResponse>,
                response: Response<TransactionsDeleteResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doDeleteTransaction.postValue(body)
                        Log.d("success","$body")
                    }else{
                        doDeleteTransaction.postValue(null)
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        message.postValue(error)
                        Log.d("errorr","${response.code()}")
                    }
                }else{
                    doDeleteTransaction.postValue(null)
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    message.postValue(error)
                    Log.d("errorr","${response.code()}")
                }
            }

            override fun onFailure(call: Call<TransactionsDeleteResponse>, t: Throwable) {
                doDeleteTransaction.postValue(null)
                Log.d("error","${t.message}")
            }

        })
    }


    private val doPostTransactionsWithCart : MutableLiveData<PostTransactionsResponse?> = MutableLiveData()
    fun doPostTransactionsWithCartObserver() : LiveData<PostTransactionsResponse?> = doPostTransactionsWithCart


    fun doPostTransactionsWithCart(token : String,waitListId : String,chairNumber :MutableList<Int>){
        api.postTransactionWithCart(token,PostTransactionsWithCartRequest(waitListId,chairNumber))
            .enqueue(object : Callback<PostTransactionsResponse>{
                override fun onResponse(
                    call: Call<PostTransactionsResponse>,
                    response: Response<PostTransactionsResponse>
                ) {
                    if(response.isSuccessful){
                        val body = response.body()
                        if(body != null){
                            doPostTransactionsWithCart.postValue(body)
                            Log.d("success","$body")
                        }else{
                            doPostTransactionsWithCart.postValue(null)
                            val error = ErrorValidation.errorAuthValidation(response.code())
                            message.postValue(error)
                            Log.d("errorr","${response.code()}")
                        }
                    }else{
                        doPostTransactionsWithCart.postValue(null)
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        message.postValue(error)
                        Log.d("errorr","${response.code()}")
                    }
                }

                override fun onFailure(call: Call<PostTransactionsResponse>, t: Throwable) {
                    doPostTransactionsWithCart.postValue(null)
                    Log.d("error","${t.message}")
                }


            })

    }



}