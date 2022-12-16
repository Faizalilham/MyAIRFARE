package binar.finalproject.MyAirFare.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.ActivityDetailTransactionsBinding
import binar.finalproject.MyAirFare.databinding.AlertRescheduleBinding
import binar.finalproject.MyAirFare.model.transactions.Transactions
import binar.finalproject.MyAirFare.utils.DatePicker
import binar.finalproject.MyAirFare.utils.MidtransConfig
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.transactions.TransactionsViewModel
import com.midtrans.sdk.corekit.core.MidtransSDK

class DetailTransactionsActivity : AppCompatActivity() {
    private var _binding : ActivityDetailTransactionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var transactionsViewModel: TransactionsViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailTransactionsBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        transactionsViewModel = ViewModelProvider(this)[TransactionsViewModel::class.java]
        setContentView(binding.root)
        setupView()
        setupCollapse()
        MidtransConfig.setupMidTrans(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupView(){
        val i = intent.getParcelableExtra<Transactions>("transactions")
        if(i != null){
            binding.apply {
                doPayment(i.token_trx)
                doReschedule(i.id)
                tvOrderId.text = i.order_id
                tvStatus.text = i.status
                i.carts.forEach {
                    tvFlightName.text = it.ticket.name
                    tvFlightNumber.text = it.ticket.flight_number
                    tvFrom.text = it.ticket.from
                    tvDestination.text = it.ticket.dest
                    when(it.ticket.kelas){
                        1 -> tvClass.text = "Economy"
                        2 -> tvClass.text = "Bussiness"
                        else -> tvClass.text = ""
                    }
                    val dateAir = DatePicker.dateCalculation(it.ticket.date_air)
                    val estimated = DatePicker.dateCalculation(it.ticket.estimated_up_dest)
                    val date = "$dateAir - $estimated"
                    tvDate.text = date
                }
                val price = "Rp. ${i.price}"
                tvPrice.text = price
            }
        }
    }

    private fun doPayment(snap : String){
        binding.btnPayment.setOnClickListener {
            MidtransSDK.getInstance().transactionRequest = MidtransConfig.initTransactionRequest()
            MidtransSDK.getInstance().startPaymentUiFlow(this,snap)
        }
    }

    private fun doReschedule(id : Int){
        binding.tvReschedule.setOnClickListener {
            alertReschedule(id)
        }
    }

    private fun rescheduleTicket(id : Int){
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
                transactionsViewModel.doDeleteTransaction(it,id)
                transactionsViewModel.doDeleteTransactionObserver().observe(this){ result ->
                    if(result != null){
                        if(result.deleted != ""){
                            finish()
                            Toast.makeText(this, "Pembatalan Penerbangan Berhasil", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        transactionsViewModel.messageObserver().observe(this){message ->
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun alertReschedule(id : Int){
        val alert = AlertDialog.Builder(this).create()
        val view = AlertRescheduleBinding.inflate(layoutInflater)
        alert.setView(view.root)
        alert.window?.attributes?.windowAnimations = R.style.myDialogAnimation
        view.apply {
            btnDoReschedule.setOnClickListener {
                rescheduleTicket(id)
            }
            btnCancel.setOnClickListener {
                alert.dismiss()
            }
        }
    }

    private fun setupCollapse(){
       binding.apply {
           linearFlight.setOnClickListener {
               if (expandableLayout.isExpanded) {
                   expandableLayout.collapse()
               } else {
                   expandableLayout.expand()
               }
           }

           linearPrice.setOnClickListener {
               if (expandableLayout2.isExpanded) {
                   expandableLayout2.collapse()
               } else {
                   expandableLayout2.expand()
               }
           }
       }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}