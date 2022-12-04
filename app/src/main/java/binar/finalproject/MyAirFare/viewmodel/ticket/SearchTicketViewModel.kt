package binar.finalproject.MyAirFare.viewmodel.ticket

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare.repository.ticket.SearchTicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchTicketViewModel @Inject constructor(
    private  val searchTicketRepository: SearchTicketRepository):ViewModel() {

        fun doSearchTicket(from : String,
        destination: String,depart : String) = searchTicketRepository.doSearchTicket(from,destination, depart)

        fun doSearchTicketObserver() = searchTicketRepository.doSearchTicketObserver()

        fun messageObserver() = searchTicketRepository.messageObserver()

}