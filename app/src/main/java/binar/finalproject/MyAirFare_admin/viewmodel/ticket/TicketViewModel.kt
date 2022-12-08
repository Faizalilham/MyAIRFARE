package binar.finalproject.MyAirFare_admin.viewmodel.ticket

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare_admin.repository.ticket.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.ticker
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    fun addTicket(
        token : String,
        airlane : RequestBody,
        from_city : RequestBody, destination : RequestBody,
        dest_air : RequestBody, price : RequestBody, no_chair : RequestBody,
        type_ticket : RequestBody, image : MultipartBody.Part, flightNumber : RequestBody,
        kelas : RequestBody, estimated_up_dest : RequestBody
    ) = ticketRepository.doAddTicket(token,airlane,from_city,destination,dest_air,price,
        no_chair,type_ticket, image, flightNumber, kelas, estimated_up_dest)
    fun doUpdateTicket(token : String, id:String,
                       airlane : RequestBody, from_city : RequestBody, destination : RequestBody,
                       dest_air : RequestBody, price : RequestBody, no_chair : RequestBody,
                       type_ticket : RequestBody, image : MultipartBody.Part, flightNumber : RequestBody,
                       kelas : RequestBody, estimated_up_dest : RequestBody) =
        ticketRepository.doUpdateTicket(token,id,airlane,from_city,destination,dest_air,price,
            no_chair,type_ticket, image, flightNumber, kelas, estimated_up_dest)

    fun doDeleteTicket(token : String, id:String) = ticketRepository.doDeleteTicket(token,id)
    fun readTicket(token :String) = ticketRepository.readTicket(token)
    fun readTicketById(token :String,id : String) = ticketRepository.doReadTicketById(token,id)


    fun readTicketByIdObserver() = ticketRepository.doReadTicketByIdObserver()
    fun readTicketObserver() = ticketRepository.readTicketObserver()
    fun addTicketObserver() = ticketRepository.doAddTicketObserver()
    fun doDeleteTicketObserver() = ticketRepository.doDeleteTicketObserver()
    fun doUpdateTicketObserver() = ticketRepository.doUpdateTicketObserver()

    fun messageObserver() = ticketRepository.messageObserver()
}