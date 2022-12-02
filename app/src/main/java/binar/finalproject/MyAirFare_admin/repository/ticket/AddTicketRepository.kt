package binar.finalproject.MyAirFare_admin.repository.ticket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.ApiEndPoint
import binar.finalproject.MyAirFare_admin.model.ticket.PostTicketResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AddTicketRepository @Inject constructor(private val api : ApiEndPoint) {

    private val doAddTicket : MutableLiveData<PostTicketResponse?> = MutableLiveData()
    fun doAddTicketObserver() : LiveData<PostTicketResponse?> = doAddTicket

    fun doAddTicket(token : String,
        airlane : RequestBody, from_city : RequestBody,destination : RequestBody,
        dest_air : RequestBody,price : RequestBody,no_chair : RequestBody,
        type_ticket : RequestBody,image : MultipartBody.Part,flightNumber : RequestBody,
        kelas : RequestBody,estimated_up_dest : RequestBody
    ){

        api.doAddTicket(token,airlane,from_city,destination,dest_air,price,
            no_chair,type_ticket, image, flightNumber, kelas, estimated_up_dest).enqueue(object :
            Callback<PostTicketResponse>{
            override fun onResponse(
                call: Call<PostTicketResponse>,
                response: Response<PostTicketResponse>
            ) {
               if(response.isSuccessful){
                   val body = response.body()
                   if(body != null){
                       doAddTicket.postValue(body)
                   }else{
                       doAddTicket.postValue(null)
                       Log.d("Error body",response.code().toString())
                   }
               }else{
                   doAddTicket.postValue(null)
                   Log.d("failed",response.code().toString())
               }
            }

            override fun onFailure(call: Call<PostTicketResponse>, t: Throwable) {
                doAddTicket.postValue(null)
                Log.d("throwable",t.message.toString())
            }

        })
    }



}