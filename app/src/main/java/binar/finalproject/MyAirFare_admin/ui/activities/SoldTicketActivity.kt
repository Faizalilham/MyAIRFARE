package binar.finalproject.MyAirFare_admin.ui.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.MyAirFare_admin.adapter.TransactionsAdapter
import binar.finalproject.MyAirFare_admin.databinding.ActivitySoldTicketBinding
import binar.finalproject.MyAirFare_admin.model.transactions.Transactions
import binar.finalproject.MyAirFare_admin.viewmodel.auth.AuthPreferencesViewModel
import binar.finalproject.MyAirFare_admin.viewmodel.transactions.TransactionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SoldTicketActivity : AppCompatActivity() {
    private var _binding : ActivitySoldTicketBinding? = null
    private val binding get()  = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var transactionsViewModel: TransactionsViewModel
    private lateinit var transactionsAdapter: TransactionsAdapter
    private var totalSalary = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySoldTicketBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        transactionsViewModel = ViewModelProvider(this)[TransactionsViewModel::class.java]
        setContentView(binding.root)
        getAllTransactions()
        back()
    }

    private fun getAllTransactions(){
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
                transactionsViewModel.doGetAllTransaction(it)
                showLoading(true)
                transactionsViewModel.doGetAllTransactionObserver().observe(this){  transactions ->
                    if(transactions != null){
                        showLoading(false)
                        transactions.transaction.forEach { trx ->
                            totalSalary += trx.price
                            Log.d("STATUS",trx.status)
                            val a = transactions.transaction.filter { its ->
                                its.status == "finished"
                            }
                            setRecycler(a.toMutableList())
                            Log.d("TOTOTD","$a")
                        }
                    }else{
                        showLoading(false)
                        transactionsViewModel.messageObserver().observe(this){ message ->
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setRecycler(data : MutableList<Transactions>){
        transactionsAdapter = TransactionsAdapter(object : TransactionsAdapter.OnClick{
            override fun onDetail(transactions: Transactions) {
//                startActivity(Intent(this, DetailTransactionsActivity::class.java).also{
//                    it.putExtra("transactions",transactions)
//                    it.putExtra("status",transactions.status)
//                })
            }
        })
        transactionsAdapter.submitData(data)
        binding.ticketRecycler.apply {
            adapter = transactionsAdapter
            layoutManager = LinearLayoutManager(this@SoldTicketActivity)
        }
    }

    private fun showLoading(loading: Boolean){
        if(loading) binding.loading.visibility = View.VISIBLE else binding.loading.visibility = View.GONE
    }

    private fun back(){
        binding.toolbar.setOnClickListener { finish() }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}