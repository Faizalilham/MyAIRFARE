package binar.finalproject.MyAirFare_admin.repository.ticket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.ApiEndPoint
import binar.finalproject.MyAirFare_admin.model.ticket.PostTicketResponse
import binar.finalproject.MyAirFare_admin.model.ticket.UpdateTicketResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UpdateTicketRepository @Inject constructor(private val api : ApiEndPoint) {

    private val doUpdateTicket : MutableLiveData<UpdateTicketResponse?> = MutableLiveData()
    fun doUpdateTicketObserver() : LiveData<UpdateTicketResponse?> = doUpdateTicket

    fun doUpdateTicket(token : String,id:String,
                    airlane : RequestBody, from_city : RequestBody, destination : RequestBody,
                    dest_air : RequestBody, price : RequestBody, no_chair : RequestBody,
                    type_ticket : RequestBody, image : MultipartBody.Part, flightNumber : RequestBody,
                    kelas : RequestBody, estimated_up_dest : RequestBody
    ){

        api.doUpdateTicket(token,id,airlane,from_city,destination,dest_air,price,
            no_chair,type_ticket, image, flightNumber, kelas, estimated_up_dest).enqueue(object :
            Callback<UpdateTicketResponse> {
            override fun onResponse(
                call: Call<UpdateTicketResponse>,
                response: Response<UpdateTicketResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doUpdateTicket.postValue(body)
                    }else{
                        doUpdateTicket.postValue(null)
                        Log.d("Error body",response.code().toString())
                    }
                }else{
                    doUpdateTicket.postValue(null)
                    Log.d("failed",response.code().toString())
                }
            }

            override fun onFailure(call: Call<UpdateTicketResponse>, t: Throwable) {
                doUpdateTicket.postValue(null)
                Log.d("throwable",t.message.toString())
            }

        })
    }
}