package binar.finalproject.MyAirFare_admin.viewmodel.ticket

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare_admin.repository.ticket.DeleteTicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class DeleteTicketViewModel @Inject constructor(
    private val deleteTicketRepository: DeleteTicketRepository)
    : ViewModel() {

    fun doDeleteTicket(token : String, id:String) = deleteTicketRepository.doDeleteTicket(token,id)

    fun doDeleteTicketObserver() = deleteTicketRepository.doDeleteTicketObserver()
}