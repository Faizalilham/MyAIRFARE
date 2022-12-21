package binar.finalproject.MyAirFare.repository.checkin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.ApiEndPoint
import binar.finalproject.MyAirFare.model.checkin.CheckinAutoRequest
import binar.finalproject.MyAirFare.model.checkin.CheckinManualRequest
import binar.finalproject.MyAirFare.model.checkin.CheckinResponse
import binar.finalproject.MyAirFare.model.transactions.PostTransactionsResponse
import binar.finalproject.MyAirFare.utils.ErrorValidation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CheckInRepository @Inject constructor(private val api : ApiEndPoint) {

    private val doCheckInAuto : MutableLiveData<CheckinResponse?> = MutableLiveData()
    fun doCheckInAutoObserver(): LiveData<CheckinResponse?> = doCheckInAuto

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message

    fun doCheckinAuto(token : String,flight_number : String){
        api.doCheckinAuto(token, CheckinAutoRequest(flight_number))
            .enqueue(object : Callback<CheckinResponse>{
                override fun onResponse(
                    call: Call<CheckinResponse>,
                    response: Response<CheckinResponse>
                ) {
                    if(response.isSuccessful){
                        val body = response.body()
                        if(body != null){
                            doCheckInAuto.postValue(body)
                            Log.d("success","$body")
                        }else{
                            doCheckInAuto.postValue(null)
                            val error = ErrorValidation.errorAuthValidation(response.code())
                            message.postValue(error)
                            Log.d("errorr","${response.code()}")
                        }
                    }else{
                        doCheckInAuto.postValue(null)
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        message.postValue("$error ${response.code()}")
                        Log.d("errorr","${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CheckinResponse>, t: Throwable) {
                    doCheckInAuto.postValue(null)
                    Log.d("error","${t.message}")
                }

            })
    }

    private val doCheckInManual : MutableLiveData<CheckinResponse?> = MutableLiveData()
    fun doCheckInManualObserver(): LiveData<CheckinResponse?> = doCheckInManual

    fun doChekInManual(token : String,ticket_id : String,order_id : String){
        api.doCheckinManual(token, CheckinManualRequest(ticket_id, order_id))
            .enqueue(object : Callback<CheckinResponse>{
                override fun onResponse(
                    call: Call<CheckinResponse>,
                    response: Response<CheckinResponse>
                ) {
                    if(response.isSuccessful){
                        val body = response.body()
                        if(body != null){
                            doCheckInManual.postValue(body)
                            Log.d("success","$body")
                        }else{
                            doCheckInManual.postValue(null)
                            val error = ErrorValidation.errorAuthValidation(response.code())
                            message.postValue(error)
                            Log.d("errorr","${response.code()}")
                        }
                    }else{
                        doCheckInManual.postValue(null)
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        message.postValue(error)
                        Log.d("errorr","${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CheckinResponse>, t: Throwable) {
                    doCheckInAuto.postValue(null)
                    Log.d("error","${t.message}")
                }

            })
    }


}