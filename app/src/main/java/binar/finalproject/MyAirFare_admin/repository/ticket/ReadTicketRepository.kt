package binar.finalproject.MyAirFare_admin.repository.ticket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.ApiEndPoint
import binar.finalproject.MyAirFare_admin.model.ticket.PostTicketResponse
import binar.finalproject.MyAirFare_admin.model.ticket.ReadTicketResponse
import binar.finalproject.MyAirFare_admin.model.ticket.Tickets
import binar.finalproject.MyAirFare_admin.utils.ErrorValidation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ReadTicketRepository @Inject constructor(private val api : ApiEndPoint) {

    private val readTicket : MutableLiveData<MutableList<Tickets>?> = MutableLiveData()
    fun readTicketObserver() : LiveData<MutableList<Tickets>?> = readTicket

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message


    fun readTicket(token : String){
        api.readAllTicket(token).enqueue(object : Callback<ReadTicketResponse>{
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
            }

        })
    }
}