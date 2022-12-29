@file:Suppress("unused", "unused")

package binar.finalproject.MyAirFare.viewmodel.checkin

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare.repository.checkin.CheckInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CheckinViewModel @Inject constructor(
    private val checkinRepository : CheckInRepository
) : ViewModel() {

        fun doCheckInAuto(token : String,flight_number : String)
        = checkinRepository.doCheckinAuto(token,flight_number)

        fun doCheckInAutoObserver() = checkinRepository.doCheckInAutoObserver()


        fun doCheckInManual(token : String,ticket_id : String, order_id : String)
        = checkinRepository.doChekInManual(token, ticket_id, order_id)

        fun doCheckInManualObserver() = checkinRepository.doCheckInManualObserver()

        fun messageObserver() = checkinRepository.messageObserver()

}