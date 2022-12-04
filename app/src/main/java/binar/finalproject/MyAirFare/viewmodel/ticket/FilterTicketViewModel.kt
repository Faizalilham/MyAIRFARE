package binar.finalproject.MyAirFare.viewmodel.ticket

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare.repository.ticket.FilterTicketRepository
import javax.inject.Inject

class FilterTicketViewModel @Inject constructor(
    private val filterTicketRepository : FilterTicketRepository):ViewModel() {

        fun doFilterTicket (from :String,dest : String,depart :String,
                            kembalian :String,kelas : String) =
            filterTicketRepository.doFilterTicket(from,dest,depart, kembalian, kelas)

        fun doFilterTicketObserver() = filterTicketRepository.doSearchTicketObserver()
}