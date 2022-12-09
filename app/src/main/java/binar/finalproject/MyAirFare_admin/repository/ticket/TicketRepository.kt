package binar.finalproject.MyAirFare_admin.repository.ticket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.api.ApiEndPoint
import binar.finalproject.MyAirFare_admin.model.ticket.*
import binar.finalproject.MyAirFare_admin.utils.ErrorValidation
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class TicketRepository @Inject constructor(private val api : ApiEndPoint) {

    private val doAddTicket : MutableLiveData<PostTicketResponse?> = MutableLiveData()
    fun doAddTicketObserver() : LiveData<PostTicketResponse?> = doAddTicket

    private val message : MutableLiveData<String> = MutableLiveData()
    fun messageObserver(): LiveData<String> = message

    fun doAddTicket(token : String,
                    airlane : RequestBody, from_city : RequestBody, destination : RequestBody,
                    dest_air : RequestBody, price : RequestBody, no_chair : RequestBody,
                    type_ticket : RequestBody, image : MultipartBody.Part, flightNumber : RequestBody,
                    kelas : RequestBody, estimated_up_dest : RequestBody
    ){

        api.doAddTicket(token,airlane,from_city,destination,dest_air,price,
            no_chair,type_ticket, image, flightNumber, kelas, estimated_up_dest).enqueue(object :
            Callback<PostTicketResponse> {
            override fun onResponse(
                call: Call<PostTicketResponse>,
                response: Response<PostTicketResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doAddTicket.postValue(body)
                        Log.d("SUCCESS","$body")
                    }else{
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        doAddTicket.postValue(null)
                        message.postValue(error)
                        Log.d("ERROR",response.code().toString())
                    }
                }else{
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    doAddTicket.postValue(null)
                    message.postValue(error)
                    Log.d("ERROR",response.code().toString())
                }
            }

            override fun onFailure(call: Call<PostTicketResponse>, t: Throwable) {
                doAddTicket.postValue(null)
                message.postValue(t.message)
            }

        })
    }

    private val readTicket : MutableLiveData<MutableList<Tickets>?> = MutableLiveData()
    fun readTicketObserver() : LiveData<MutableList<Tickets>?> = readTicket

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
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        message.postValue(error)
                        Log.d("ERROR",error)
                    }
                }else{
                    doUpdateTicket.postValue(null)
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    message.postValue(error)
                    Log.d("ERROR",error)
                }
            }

            override fun onFailure(call: Call<UpdateTicketResponse>, t: Throwable) {
                doUpdateTicket.postValue(null)
                message.postValue(t.message)
                Log.d("throwable",t.message.toString())
            }

        })
    }

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
                        val error = ErrorValidation.errorAuthValidation(response.code())
                        message.postValue(error)
                        Log.d("Error body",response.code().toString())
                    }
                }else{
                    doDeleteTicket.postValue(null)
                    val error = ErrorValidation.errorAuthValidation(response.code())
                    message.postValue(error)
                    Log.d("failed",response.code().toString())
                }
            }

            override fun onFailure(call: Call<DeleteTicketResponse>, t: Throwable) {
                doDeleteTicket.postValue(null)
                message.postValue(t.message)
                Log.d("throwable",t.message.toString())
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