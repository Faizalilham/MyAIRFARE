package binar.finalproject.MyAirFare.repository.checkin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.MyAirFare.model.room.CheckIn
import binar.finalproject.MyAirFare.room.DaoCheckIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CheckInRoomRepository @Inject constructor(private val dao : DaoCheckIn) {

    private val getAllCheckIn : MutableLiveData<MutableList<CheckIn>> = MutableLiveData()
    fun getAllCheckInObserver() : LiveData<MutableList<CheckIn>> = getAllCheckIn

    private val insertCheckIn : MutableLiveData<Unit> = MutableLiveData()
    fun insertCheckInObserver() : LiveData<Unit> = insertCheckIn

    private val deleteCheckIn : MutableLiveData<Unit> = MutableLiveData()
    fun deleteCheckInObserver() : LiveData<Unit> = deleteCheckIn

    fun getAllCheckIn(){
        CoroutineScope(Dispatchers.IO).launch {
            getAllCheckIn.postValue(dao.getAllCheckIn())
        }
    }

    fun insertCheckIn(checkIn : CheckIn){
        CoroutineScope(Dispatchers.IO).launch {
           insertCheckIn.postValue(dao.insertCheckIn(checkIn))
        }
    }

    fun deleteCheckIn(checkin: CheckIn){
        CoroutineScope(Dispatchers.IO).launch {
            deleteCheckIn.postValue(dao.deleteCheckIn(checkin))
        }
    }
}