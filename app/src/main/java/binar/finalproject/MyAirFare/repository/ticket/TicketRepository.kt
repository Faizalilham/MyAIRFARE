package binar.finalproject.MyAirFare.repository.ticket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.ApiEndPoint
import binar.finalproject.MyAirFare.model.tickets.ReadTicketByIdResponse
import binar.finalproject.MyAirFare.model.tickets.ReadTicketResponse
import binar.finalproject.MyAirFare.model.tickets.Schedule
import binar.finalproject.MyAirFare.utils.ErrorValidation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class TicketRepository @Inject constructor(private val api : ApiEndPoint) {

    private val readTicket : MutableLiveData<MutableList<Schedule>?> = MutableLiveData()
    fun readTicketObserver() : LiveData<MutableList<Schedule>?> = readTicket

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message


    fun readTicket(token : String){
        api.readAllTicket(token).enqueue(object : Callback<ReadTicketResponse> {
            override fun onResponse(
                call: Call<ReadTicketResponse>,
                response: Response<ReadTicketResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        readTicket.postValue(body.tickets)
                        Log.d("SUCCESS","$body")
                    }else{
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        readTicket.postValue(null)
                        message.postValue(error)
                        Log.d("ERROR",response.code().toString())
                    }
                }else{
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    readTicket.postValue(null)
                    message.postValue(error)
                    Log.d("ERROR",response.code().toString())
                }
            }

            override fun onFailure(call: Call<ReadTicketResponse>, t: Throwable) {
                readTicket.postValue(null)
                message.postValue(t.message)
                Log.d("ERROR",t.message.toString())



            }

        })
    }

    private val doReadTicketById : MutableLiveData<ReadTicketByIdResponse?> = MutableLiveData()
    fun doReadTicketByIdObserver() : LiveData<ReadTicketByIdResponse?> = doReadTicketById

    fun doReadTicketById(token :String,id : String){
        api.readTicketById(token,id).enqueue(object : Callback<ReadTicketByIdResponse>{
            override fun onResponse(
                call: Call<ReadTicketByIdResponse>,
                response: Response<ReadTicketByIdResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doReadTicketById.postValue(body)
                        Log.d("SUCCESS","$body")
                    }else{
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        doReadTicketById.postValue(null)
                        message.postValue(error)
                        Log.d("ERROR",response.code().toString())
                    }
                }else{
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    doReadTicketById.postValue(null)
                    message.postValue(error)
                    Log.d("ERROR",response.code().toString())
                }
            }

            override fun onFailure(call: Call<ReadTicketByIdResponse>, t: Throwable) {
                doReadTicketById.postValue(null)
                message.postValue(t.message)
            }

        })
    }
}