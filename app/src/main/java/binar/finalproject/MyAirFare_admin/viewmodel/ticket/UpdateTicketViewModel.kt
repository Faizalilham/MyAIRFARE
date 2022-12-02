package binar.finalproject.MyAirFare_admin.viewmodel.ticket

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare_admin.repository.ticket.UpdateTicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UpdateTicketViewModel @Inject constructor(private val updateTicketRepository: UpdateTicketRepository)
    : ViewModel() {

    fun doUpdateTicket(token : String, id:String,
                       airlane : RequestBody, from_city : RequestBody, destination : RequestBody,
                       dest_air : RequestBody, price : RequestBody, no_chair : RequestBody,
                       type_ticket : RequestBody, image : MultipartBody.Part, flightNumber : RequestBody,
                       kelas : RequestBody, estimated_up_dest : RequestBody) =
        updateTicketRepository.doUpdateTicket(token,id,airlane,from_city,destination,dest_air,price,
            no_chair,type_ticket, image, flightNumber, kelas, estimated_up_dest)

    fun doUpdateTicketObserver() = updateTicketRepository.doUpdateTicketObserver()
}