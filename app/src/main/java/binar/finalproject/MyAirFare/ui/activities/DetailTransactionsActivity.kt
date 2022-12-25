package binar.finalproject.MyAirFare.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTransactionsActivity : AppCompatActivity() {
    private var _binding : ActivityDetailTransactionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var transactionsViewModel: TransactionsViewModel
    private var id : Int? = null
    private var dateAir = ""
    private val chairs = mutableListOf<Int>()
    private val flightNumber = mutableListOf<String>()
    private val flightName = mutableListOf<String>()


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
        id = intent.getIntExtra("id",0)
        if(id != null){
            setupVisibility(id!!)
        }
        back()
        doCheckIn()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupView(){
        val i = intent.getParcelableExtra<Transactions>("transactions")
        val status = intent.getStringExtra("status")
        if(i != null && status != null){
            binding.apply {
                when(status){
                    "pending-payment" ->  doPayment(i.token_trx)
                    "finish","expired" ->  btnPayment.isEnabled = false
                }
                doReschedule(i.id)
                tvOrderId.text = "Nomor Pesanan ${i.order_id}"
                tvStatus.text = i.status
                i.carts.forEach {
                    if(i.carts.size > 1){
                        if(it.chair_number != null){
                            chairs.add(it.chair_number)
                            val chairsResult = chairs.sorted().joinToString(",")
                            tvChairs.text = chairsResult
                        }
                        flightNumber.add(it.ticket.flight_number)
                        flightName.add(it.ticket.name)
                    }else{
                        tvChairs.text = it.chair_number.toString()
                        tvFlightNumber.text = it.ticket.flight_number
                        tvFlightName.text = it.ticket.name
                    }
                }

                if(flightName.size > 1 || flightNumber.size > 1){
                    val flightNumberResult = "${flightNumber[1]},${flightNumber[0]}"
                    tvFlightNumber.text = flightNumberResult
                    val flightNameResult = "${flightName[1]},${flightName[0]}"
                    tvFlightName.text = flightNameResult
                }

                i.carts.forEach {
                    dateAir = it.ticket.date_air
                    tvFrom.text = it.ticket.from
                    tvDestination.text = it.ticket.dest
                    when(it.ticket.kelas){
                        1 -> tvClass.text = "Economy"
                        2 -> tvClass.text = "Bussiness"
                        else -> tvClass.text = ""
                    }
                    when(it.ticket.type){
                        1 -> {
                            if(chairs.size > 1){
                                tvType.text = "Dewasa - Pulang Pergi"
                            }else{
                                tvType.text = "Dewasa - Sekali Jalan"
                            }
                        }
                        2 ->{
                            if(chairs.size > 1){
                                tvType.text = "Anak anak - Pulang Pergi"
                            }else{
                                tvType.text = "Anak anak - Sekali Jalan"
                            }
                        }
                    }

                    val dateAir = DatePicker.dateCalculation(it.ticket.date_air)
                    val estimated = DatePicker.dateCalculation(it.ticket.estimated_up_dest)
                    val date = "$dateAir - $estimated"
                    tvDate.text = date
                }
                val price = "Rp. ${i.price}"
                tvPrice.text = price
                tvPriceReturn.text = price
            }
        }
    }

    private fun setupVisibility(id : Int){
        when(id){
            1 -> {
                binding.apply {
                    tvTittleQR.visibility = View.GONE
                    tvSubTittleQR.visibility = View.GONE
                    btnScanQR.visibility = View.GONE
                }
            }
            2 -> {
                binding.apply {
                    tvTittlePayment.visibility = View.GONE
                    tvSubTittlePayment.visibility = View.GONE
                    btnPayment.visibility = View.GONE
                    tvLainnya.visibility = View.GONE
                    tvReschedule.visibility = View.GONE
                }
            }
            3 -> {
                binding.apply {
                    tvTittleQR.visibility = View.GONE
                    tvSubTittleQR.visibility = View.GONE
                    btnScanQR.visibility = View.GONE
                    tvTittlePayment.visibility = View.GONE
                    tvSubTittlePayment.visibility = View.GONE
                    btnPayment.visibility = View.GONE
                    tvReschedule.text = "Hapus Transaksi"
                }
            }
        }
    }

    private fun doPayment(snap : String){
        binding.btnPayment.setOnClickListener {
            MidtransSDK.getInstance().transactionRequest = MidtransConfig.initTransactionRequest()
            MidtransSDK.getInstance().startPaymentUiFlow(this,snap)
        }
    }

    private fun doReschedule(id : String){
        binding.tvReschedule.setOnClickListener {
            alertReschedule(id)
        }
    }

    private fun rescheduleTicket(id : String){
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
                transactionsViewModel.doDeleteTransaction(it,id)
                transactionsViewModel.doDeleteTransactionObserver().observe(this){ result ->
                    if(result != null){
                        if(result.deleted != 0){
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

    private fun alertReschedule(id : String){
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
        alert.show()
    }

    private fun setupCollapse(){
       binding.apply {
           linearFlight.setOnClickListener {
               if (expandableLayout.isExpanded) {
                   expandableLayout.collapse()
                   arrowLinearFlight.setImageResource(R.drawable.ic_arrow_down)
               } else {
                   expandableLayout.expand()
                   arrowLinearFlight.setImageResource(R.drawable.ic_arrow_up)
               }
           }

           linearPrice.setOnClickListener {
               if (expandableLayout2.isExpanded) {
                   expandableLayout2.collapse()
                   arrowLinearPrice.setImageResource(R.drawable.ic_arrow_down)
               } else {
                   expandableLayout2.expand()
                   arrowLinearPrice.setImageResource(R.drawable.ic_arrow_up)
               }
           }
       }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doCheckIn(){
        if(dateAir.isNotEmpty()){
            if(DatePicker.dateTimeCalculation(dateAir)){
                binding.btnScanQR.setOnClickListener {
                    startActivity(Intent(this,ScanQRActivity::class.java))
                }

            }else{
                binding.btnScanQR.isEnabled = false
                Toast.makeText(this, "Sudah terlewat untuk melakukan checkin, silahkan pesan tiket kembali", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun back(){
        binding.toolbar.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}