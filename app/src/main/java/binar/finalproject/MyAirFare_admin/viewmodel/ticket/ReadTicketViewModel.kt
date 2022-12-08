package binar.finalproject.MyAirFare_admin.viewmodel.ticket

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare_admin.repository.ticket.ReadTicketRepository
import javax.inject.Inject

class ReadTicketViewModel @Inject constructor(
    private val readTicketRepository: ReadTicketRepository):ViewModel() {

        fun readTicket(token :String) = readTicketRepository.readTicket(token)
        fun readTicketObserver() = readTicketRepository.readTicketObserver()

        fun messageObserver() = readTicketRepository.messageObserver()
}