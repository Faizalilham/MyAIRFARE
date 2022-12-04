package binar.finalproject.MyAirFare.repository.ticket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.ApiEndPoint
import binar.finalproject.MyAirFare.model.flight.ScheduleResponse
import binar.finalproject.MyAirFare.utils.ErrorValidation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SearchTicketRepository @Inject constructor(private val api : ApiEndPoint) {

    private val doSearchTicket : MutableLiveData<ScheduleResponse?> = MutableLiveData()
    fun doSearchTicketObserver() : LiveData<ScheduleResponse?> = doSearchTicket

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message

    fun doSearchTicket(from : String,destination : String,depart : String){
        api.doSearch(from,destination,depart).enqueue(object : Callback<ScheduleResponse>{
            override fun onResponse(
                call: Call<ScheduleResponse>,
                response: Response<ScheduleResponse>
            ) {

                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doSearchTicket.postValue(body)
                        Log.d("success","$body")
                    }else{
                        doSearchTicket.postValue(null)
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        message.postValue(error)
                    }
                }else{
                    doSearchTicket.postValue(null)
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    message.postValue(error)
                }
            }

            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                doSearchTicket.postValue(null)
                message.postValue(t.message.toString())

            }

        })
    }
}