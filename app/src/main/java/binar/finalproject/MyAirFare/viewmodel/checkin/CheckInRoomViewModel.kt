package binar.finalproject.MyAirFare.viewmodel.checkin

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare.model.room.CheckIn
import binar.finalproject.MyAirFare.repository.checkin.CheckInRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckInRoomViewModel @Inject constructor(
    private val checkInRoomRepository: CheckInRoomRepository): ViewModel() {

    fun getAllCheckIn() = checkInRoomRepository.getAllCheckIn()
    fun getAllCheckInObserver() = checkInRoomRepository.getAllCheckInObserver()

    fun insertCheckIn(checkIn : CheckIn) = checkInRoomRepository.insertCheckIn(checkIn)
    fun insertCheckInObserver() = checkInRoomRepository.insertCheckInObserver()

    fun deleteCheckIn(checkIn : CheckIn) = checkInRoomRepository.deleteCheckIn(checkIn)
    fun deleteCheckInObserver() = checkInRoomRepository.deleteCheckInObserver()
}