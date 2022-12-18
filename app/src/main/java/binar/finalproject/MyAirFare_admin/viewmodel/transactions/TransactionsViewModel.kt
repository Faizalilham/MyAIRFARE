package binar.finalproject.MyAirFare_admin.viewmodel.transactions

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare_admin.repository.transactions.TransactionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionsRepository : TransactionsRepository
):ViewModel() {

    fun doGetAllTransaction(token : String) = transactionsRepository.doGetAllTransactions(token)
    fun doGetAllTransactionObserver()  = transactionsRepository.getAllTransactionsObserver()

    fun messageObserver() = transactionsRepository.messageObserver()

}