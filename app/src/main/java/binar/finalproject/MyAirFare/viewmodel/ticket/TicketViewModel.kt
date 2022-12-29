@file:Suppress("unused", "unused", "unused", "unused", "unused")

package binar.finalproject.MyAirFare.viewmodel.ticket

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare.repository.ticket.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel() {


    fun readTicket(token :String) = ticketRepository.readTicket(token)
    fun readTicketById(token :String,id : String) = ticketRepository.doReadTicketById(token,id)


    fun readTicketByIdObserver() = ticketRepository.doReadTicketByIdObserver()
    fun readTicketObserver() = ticketRepository.readTicketObserver()


    fun messageObserver() = ticketRepository.messageObserver()
}