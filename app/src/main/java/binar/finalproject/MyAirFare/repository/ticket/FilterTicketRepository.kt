package binar.finalproject.MyAirFare.repository.ticket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.ApiEndPoint
import binar.finalproject.MyAirFare.model.tickets.ScheduleResponse
import binar.finalproject.MyAirFare.utils.ErrorValidation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class FilterTicketRepository @Inject constructor(private val api : ApiEndPoint) {

    private val doFilterTicket : MutableLiveData<ScheduleResponse?> = MutableLiveData()
    fun doSearchTicketObserver() : LiveData<ScheduleResponse?> = doFilterTicket

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message

    fun doFilterTicket(
        from :String,dest : String,depart :String,
        kembalian :String,kelas : String

    ){
        api.doFilterTicket(from,dest,depart, kembalian, kelas).enqueue(object :
            Callback<ScheduleResponse>{
            override fun onResponse(
                call: Call<ScheduleResponse>,
                response: Response<ScheduleResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doFilterTicket.postValue(body)
                        Log.d("success","$body")
                    }else{
                        doFilterTicket.postValue(null)
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        message.postValue(error)
                    }
                }else{
                    doFilterTicket.postValue(null)
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    message.postValue(error)
                }
            }

            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                doFilterTicket.postValue(null)
            }

        })
    }
}