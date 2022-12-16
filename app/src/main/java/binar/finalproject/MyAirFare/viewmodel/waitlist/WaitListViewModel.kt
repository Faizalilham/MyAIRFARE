package binar.finalproject.MyAirFare.viewmodel.waitlist

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare.repository.waitlist.WaitListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.internal.wait
import javax.inject.Inject


@HiltViewModel
class WaitListViewModel @Inject constructor(
    private val waitListRepository: WaitListRepository
) : ViewModel() {

    fun getAllWaitList(token : String) = waitListRepository.getAllWaitList(token)
    fun getAllWaitListObserver () = waitListRepository.getAllWaitListObserver()

    fun postWaitList(token : String,tickets_id : MutableList<String>) = waitListRepository.postWaitList(token,tickets_id)
    fun postWaitListObserver() = waitListRepository.postWaitListObserver()

    fun deleteWaitList(token : String,id : Int) = waitListRepository.deleteWaitList(token,id)
    fun deleteWaitListObserver() = waitListRepository.deleteWaitListObserver()

    fun messageObserver() = waitListRepository.messageObserver()


}