package binar.finalproject.MyAirFare.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.ActivityDetailPerjalananBinding
import binar.finalproject.MyAirFare.databinding.AlertLoginBinding
import binar.finalproject.MyAirFare.model.tickets.Schedule
import binar.finalproject.MyAirFare.utils.DatePicker
import binar.finalproject.MyAirFare.utils.MidtransConfig
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.transactions.TransactionsViewModel
import binar.finalproject.MyAirFare.viewmodel.waitlist.WaitListViewModel
import com.bumptech.glide.Glide
import com.midtrans.sdk.corekit.core.MidtransSDK
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPerjalanan : AppCompatActivity(), AdapterView.OnItemClickListener {
    private var _binding : ActivityDetailPerjalananBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var waitListViewModel : WaitListViewModel
    private lateinit var transactionsViewModel : TransactionsViewModel
    private var ticket_id = mutableListOf<String>()
    private var chairsNumber = mutableListOf<Int>()
    private var totalPrice = mutableListOf<Int>()
    var returnFlight = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailPerjalananBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        waitListViewModel = ViewModelProvider(this)[WaitListViewModel::class.java]
        transactionsViewModel = ViewModelProvider(this)[TransactionsViewModel::class.java]
        setContentView(binding.root)
        val schedule = intent.getParcelableExtra<Schedule>("schedule")
        val scheduleReturn = intent.getParcelableArrayListExtra<Schedule>("returnFlight")
        val chairs = intent.getIntegerArrayListExtra("chairs")?.toMutableList()
        back()
        setupView(schedule,chairs)
        if(scheduleReturn != null){
            if(scheduleReturn.size > 0){
                binding.linearReturnFlight.visibility = View.VISIBLE
                setupViewReturn(scheduleReturn,chairs)
                returnFlight = true
            }else{
                binding.linearReturnFlight.visibility = View.GONE
                binding.tvPriceReturn.visibility = View.GONE
            }
        }
        MidtransConfig.setupMidTrans(this)
        setupViewPrice()
        doBooking()
        waitList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupView(i : Schedule? ,chairs : MutableList<Int>?){
        if(i != null && chairs != null){
            binding.apply {
                ticket_id.add(i.id)
                dropDownMenu(chairs,R.layout.dropdown_tittle_item,tvNoChair)
                Glide.with(root).load("https://binarstudpenfinalprojectbe-production.up.railway.app${i.logo}").into(imageLogo)
                tvCode.text = i.flight_number
                tvFrom.text = i.from
                tvDestination.text = i.dest
                tvAirlane.text = i.name
                when(i.kelas){
                    1 -> tvClass.text = "ECONOMY"
                    2 -> tvClass.text = "BUSSINESS"
                    else -> tvClass.text = ""
                }
                when(i.type){
                    1 -> tvType.text = "DEWASA"
                    2 -> tvType.text = "ANAK-ANAK"
                }
                val price = "Rp. ${i.price}"
                totalPrice.add(i.price)
                tvPrice.text = price
                tvChairs.text = i.no_chair.toString()
                val datesFrom = DatePicker.dateCalculation(i.date_air)
                val timeFrom =  DatePicker.timeCalculation(i.date_air)
                val datesDest = DatePicker.dateCalculation(i.estimated_up_dest)
                val timeDest =  DatePicker.timeCalculation(i.estimated_up_dest)
                val datesAir = """
                    $datesFrom
                    $timeFrom
                """.trimIndent()
                val estimatedAir = """
                    $datesDest
                    $timeDest
                """.trimIndent()
                tvDateAir.text = datesAir
                tvEstimated.text = estimatedAir
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupViewReturn(scheduleReturn : MutableList<Schedule>?, chairs : MutableList<Int>?){
        if(scheduleReturn != null && chairs != null){
            binding.apply {
                scheduleReturn.forEach { i ->
                    ticket_id.add(i.id)
                    Log.d("KONNTOL","$i")
                    dropDownMenu(chairs,R.layout.dropdown_tittle_item,tvNoChairReturn)
                    Glide.with(root).load("https://binarstudpenfinalprojectbe-production.up.railway.app${i.logo}").into(imageLogoReturn)
                    tvCodeReturn.text = i.flight_number
                    tvFromReturn.text = i.from
                    tvDestinationReturn.text = i.dest
                    tvAirlaneReturn.text = i.name
                    when(i.kelas){
                        1 -> tvClassReturn.text = "ECONOMY"
                        2 -> tvClassReturn.text = "BUSSINESS"
                        else -> tvClassReturn.text = ""
                    }
                    val price = "Rp. ${i.price}"
                    totalPrice.add(i.price)
                    tvPriceReturn.text = price
                    tvChairsReturn.text = i.no_chair.toString()
                    val datesFrom = DatePicker.dateCalculation(i.date_air)
                    val timeFrom =  DatePicker.timeCalculation(i.date_air)
                    val datesDest = DatePicker.dateCalculation(i.estimated_up_dest)
                    val timeDest =  DatePicker.timeCalculation(i.estimated_up_dest)
                    val datesAir = """
                    $datesFrom
                    $timeFrom
                """.trimIndent()
                    val estimatedAir = """
                    $datesDest
                    $timeDest
                """.trimIndent()
                    tvDateAirReturn.text = datesAir
                    tvEstimatedReturn.text = estimatedAir
                }

            }
        }
    }

    private fun setupViewPrice(){
        val result = totalPrice.sum()
        binding.tvTotalPrice.text = result.toString()
    }

    private fun dropDownMenu(arr : MutableList<Int>, @LayoutRes layout : Int, tv : AutoCompleteTextView){
        val adapter = ArrayAdapter(this, layout,arr)
        with(tv){
            setAdapter(adapter)
            onItemClickListener = this@DetailPerjalanan
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        binding.apply {
            if (parent == binding.tvNoChair) {
                tvNoChair.setText(parent.getItemAtPosition(position).toString())
            } else if (parent == binding.tvNoChairReturn) {
                tvNoChairReturn.setText(parent.getItemAtPosition(position).toString())
            }

        }

    }

    private fun doBooking(){
        binding.btnBooking.setOnClickListener { _ ->
            val waitList = intent.getBooleanExtra("waitList",false)
            if(waitList){
                postWithCart()
            }else{
                postWithSearch()

            }
        }
    }

    private fun postWithSearch(){
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
                chairsNumber.add(binding.tvNoChair.text.toString().toInt())
                if(returnFlight){
                    chairsNumber.add(binding.tvNoChairReturn.text.toString().toInt())
                }
                if(chairsNumber.size > 0){
                    if(ticket_id.size > 1){
                        ticket_id[1].trim()
                    }
                    Log.d("TIKET ID", ticket_id.toString())
                    transactionsViewModel.doPostTransactions(
                        it,
                        ticket_id,
                        chairsNumber
                    )
                    showLoading(true)
                    transactionsViewModel.doPostTransactionsObserver().observe(this){ result ->
                        if(result != null){
                            showLoading(false)
                            getSnapToken(result.trx.token_trx)
                            Toast.makeText(this, result.trx.token_trx, Toast.LENGTH_SHORT).show()
                        }else{
                            transactionsViewModel.messageObserver().observe(this){ message ->
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            }
                            showLoading(false)
                        }
                    }
                }else{
                    Toast.makeText(this, "Pilih kursi terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }else{
                alertLogin()
            }
        }
    }

    private fun postWithCart(){
        val id = intent.getStringExtra("waitListId")
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
                Log.d("TIKET ID",ticket_id.toString())
                chairsNumber.add(binding.tvNoChair.text.toString().toInt())
                if(returnFlight){
                    chairsNumber.add(binding.tvNoChairReturn.text.toString().toInt())
                }
                if(chairsNumber.size > 0){
                    transactionsViewModel.doPostTransactionsWithCart(
                        it,
                        id!!,
                        chairsNumber
                    )
                    showLoading(true)
                    transactionsViewModel.doPostTransactionsWithCartObserver().observe(this){ result ->
                        if(result != null){
                            showLoading(false)
                            getSnapToken(result.trx.token_trx)
                            Toast.makeText(this, result.trx.token_trx, Toast.LENGTH_SHORT).show()
                        }else{
                            transactionsViewModel.messageObserver().observe(this){ message ->
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            }
                            showLoading(false)
                        }
                    }
                }else{
                    Toast.makeText(this, "Pilih kursi terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun waitList(){
        binding.btnCart.setOnClickListener {
            authPreferencesViewModel.getToken().observe(this){
                if(it != null && it != "undefined"){
                    postWaitList()
                }else{
                    alertLogin()
                }
            }
        }
    }

    private fun postWaitList() {
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
                Log.d("koonntoll","$it $ticket_id")
                waitListViewModel.postWaitList(it,ticket_id)
                waitListViewModel.postWaitListObserver().observe(this){ waitList ->
                    if(waitList != null){
                        Toast.makeText(this, "Tiket Berhasil Masuk Keranjang", Toast.LENGTH_SHORT).show()
                    }else{
                        waitListViewModel.messageObserver().observe(this){ message ->
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun alertLogin(){
        val alert = AlertDialog.Builder(this).create()
        val view =  AlertLoginBinding.inflate(layoutInflater)

        alert.setView(view.root)
        alert.window?.attributes?.windowAnimations = R.style.myDialogAnimation
        view.apply {
            btnLogin.setOnClickListener {
                startActivity(Intent(this@DetailPerjalanan,LoginActivity::class.java))
            }
            btnCancel.setOnClickListener {
                alert.dismiss()
            }
        }
        alert.show()
    }

    private fun back(){
        binding.toolbar.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun getSnapToken(snap : String){
        MidtransSDK.getInstance().transactionRequest = MidtransConfig.initTransactionRequest()
        MidtransSDK.getInstance().startPaymentUiFlow(this,snap) // "6fdeefde-f5ff-40cb-a7eb-58cf63d6d88b"
    }

    private fun showLoading(loadingShow : Boolean){
        binding.apply {
            if(loadingShow){
                loadingBg.visibility = View.VISIBLE
                loading.visibility = View.VISIBLE
            }else{
                loadingBg.visibility = View.GONE
                loading.visibility = View.GONE
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}