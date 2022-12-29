package binar.finalproject.MyAirFare.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.MyAirFare.adapter.TransactionsAdapter
import binar.finalproject.MyAirFare.databinding.FragmentInActiveTicketBinding
import binar.finalproject.MyAirFare.model.transactions.Transactions
import binar.finalproject.MyAirFare.ui.activities.DetailTransactionsActivity
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.transactions.TransactionsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InActiveTicketFragment : Fragment() {


    private lateinit var binding : FragmentInActiveTicketBinding
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var transactionsViewModel : TransactionsViewModel
    private lateinit var transactionsAdapter: TransactionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInActiveTicketBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        transactionsViewModel = ViewModelProvider(this)[TransactionsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllTransactions()
    }

    private fun getAllTransactions(){
        authPreferencesViewModel.getToken().observe(requireActivity()){
            if(it != null && it != "undefined"){
                transactionsViewModel.doGetAllTransaction(it)
                showLoading(true)
                transactionsViewModel.doGetAllTransactionObserver().observe(requireActivity()){  transactions ->
                    if(transactions != null){
                        showLoading(false)
                        transactions.transaction.forEach { trx ->
                            Log.d("STATUS",trx.status)
                            val a = transactions.transaction.filter { its ->
                                its.status == "expired"
                            }
                            if(a.isNotEmpty()){
                                showWarning(false)
                                setRecycler(a.toMutableList())
                                Log.d("TOTOTD","$a")
                            }else{
                                showWarning(true)
                            }
                        }
                    }else{
                        showLoading(false)
                        transactionsViewModel.messageObserver().observe(requireActivity()){ message ->
                            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }


    private fun setRecycler(data : MutableList<Transactions>){
        transactionsAdapter = TransactionsAdapter(object : TransactionsAdapter.OnClick{
            override fun onDetail(transactions: Transactions) {
                startActivity(Intent(requireActivity(), DetailTransactionsActivity::class.java).also{
                    it.putExtra("transactions",transactions)
                    it.putExtra("status",transactions.status)
                    it.putExtra("id",3)
                })
            }
        })
        transactionsAdapter.submitData(data)
        binding.ticketRecycler.apply {
            adapter = transactionsAdapter
            if (isAdded && activity != null) {
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }
    }

    private fun showLoading(loading: Boolean){
        if(loading) binding.loading.visibility = View.VISIBLE else binding.loading.visibility = View.GONE
    }

    private fun showWarning(show : Boolean){
        binding.apply {
            if(show){
                imageNotFound.visibility = View.VISIBLE
                tvNotFound.visibility = View.VISIBLE
            }else{
                imageNotFound.visibility = View.GONE
                tvNotFound.visibility = View.GONE
            }
        }
    }


}