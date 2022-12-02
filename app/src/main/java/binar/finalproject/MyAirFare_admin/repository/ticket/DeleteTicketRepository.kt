package binar.finalproject.MyAirFare_admin.repository.ticket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.ApiEndPoint
import binar.finalproject.MyAirFare_admin.model.ticket.DeleteTicketResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DeleteTicketRepository @Inject constructor(private val api : ApiEndPoint) {

    private val doDeleteTicket : MutableLiveData<DeleteTicketResponse?> = MutableLiveData()
    fun doDeleteTicketObserver() : LiveData<DeleteTicketResponse?> = doDeleteTicket

    fun doDeleteTicket(token : String, id:String){
        api.doDeleteTicket(token,id).enqueue(object :
            Callback<DeleteTicketResponse> {
            override fun onResponse(
                call: Call<DeleteTicketResponse>,
                response: Response<DeleteTicketResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doDeleteTicket.postValue(body)
                    }else{
                        doDeleteTicket.postValue(null)
                        Log.d("Error body",response.code().toString())
                    }
                }else{
                    doDeleteTicket.postValue(null)
                    Log.d("failed",response.code().toString())
                }
            }

            override fun onFailure(call: Call<DeleteTicketResponse>, t: Throwable) {
                doDeleteTicket.postValue(null)
                Log.d("throwable",t.message.toString())
            }

        })
    }
}