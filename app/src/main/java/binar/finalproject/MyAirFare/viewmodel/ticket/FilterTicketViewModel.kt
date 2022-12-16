package binar.finalproject.MyAirFare.viewmodel.ticket

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare.repository.ticket.FilterTicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterTicketViewModel @Inject constructor(
    private val filterTicketRepository : FilterTicketRepository):ViewModel() {

        fun doFilterTicket (from :String,dest : String,depart :String,
                            kelas : String,tipe : String,dateReturn :String) =
            filterTicketRepository.doFilterTicket(from,dest,depart,kelas,tipe,dateReturn)

        fun doFilterTicketObserver() = filterTicketRepository.doSearchTicketObserver()
}