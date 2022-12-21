package binar.finalproject.MyAirFare.viewmodel.transactions

import androidx.lifecycle.ViewModel
import binar.finalproject.MyAirFare.repository.transactions.TransactionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionsRepository  : TransactionsRepository):ViewModel() {

        fun doPostTransactions(token : String,
                               ticketId : MutableList<String>,
                               chairs : MutableList<Int>) = transactionsRepository.doPostTransactions(token, ticketId, chairs)

        fun doPostTransactionsObserver() = transactionsRepository.postTransactionsObserver()

        fun doPostTransactionsWithCart(token : String,
        waitListId : String,
        chairs : MutableList<Int>) = transactionsRepository.doPostTransactionsWithCart(token, waitListId, chairs)

        fun doPostTransactionsWithCartObserver() = transactionsRepository.doPostTransactionsWithCartObserver()

        fun doGetAllTransaction(token : String) = transactionsRepository.doGetAllTransactions(token)
        fun doGetAllTransactionObserver()  = transactionsRepository.getAllTransactionsObserver()

        fun doGetTransactionById(token : String,id :Int) = transactionsRepository.doGetTransactionById(token,id)
        fun doGetTransactionByIdObserver()  = transactionsRepository.getTransactionByIdObserver()

        fun doDeleteTransaction(token : String,id : String) = transactionsRepository.doDeleteTransaction(token,id)
        fun doDeleteTransactionObserver() = transactionsRepository.deleteTransactionObserver()

        fun messageObserver() = transactionsRepository.messageObserver()


}