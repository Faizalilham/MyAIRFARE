package binar.finalproject.MyAirFare_admin.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare_admin.R
import binar.finalproject.MyAirFare_admin.databinding.ActivityDetailTicketBinding
import binar.finalproject.MyAirFare_admin.databinding.AlertDeleteTicketBinding
import binar.finalproject.MyAirFare_admin.utils.DatePicker
import binar.finalproject.MyAirFare_admin.viewmodel.auth.AuthPreferencesViewModel
import binar.finalproject.MyAirFare_admin.viewmodel.ticket.TicketViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailTicketActivity : AppCompatActivity() {
    private var _binding : ActivityDetailTicketBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var ticketViewModel: TicketViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailTicketBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        ticketViewModel = ViewModelProvider(this)[TicketViewModel::class.java]
        setContentView(binding.root)
        readTicket()
        deleteTicket()
        back()
    }

    private fun back(){
        binding.toolbar.setOnClickListener { finish() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun readTicket(){
        val id = intent.getStringExtra("id")
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
               if(id != null){
                   Log.d("TIKET","$it  $id")
                   ticketViewModel.readTicketById(it,id)
                   showLoading(true)
                   ticketViewModel.readTicketByIdObserver().observe(this){ ticket ->
                       if(ticket != null){
                           showLoading(false)
                           setupView(
                               ticket.ticket.logo,
                               ticket.ticket.name,
                               ticket.ticket.from,
                               ticket.ticket.dest,
                               ticket.ticket.no_chair,
                               ticket.ticket.flight_number,
                               ticket.ticket.type,
                               ticket.ticket.price,
                               ticket.ticket.date_air,
                               ticket.ticket.estimated_up_dest,
                               ticket.ticket.kelas,
                           )
                       }
                   }
               }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setupView(
        image : String,airlane : String,from : String,
        dest : String,chairs : Int,code : String,type : Int,price : Int,
        date :String,estimated : String,kelas : Int
    ){

        val datesFrom = DatePicker.dateCalculation(date)
        val timeFrom =  DatePicker.timeCalculation(date)
        val datesDest = DatePicker.dateCalculation(estimated)
        val timeDest =  DatePicker.timeCalculation(estimated)
        val datesAir = """
            $datesFrom
            $timeFrom
           
        """.trimIndent()
        val estimatedAir = """
            $datesDest
            $timeDest
           
        """.trimIndent()
        binding.apply {
            tvChairs.text = chairs.toString()
            when(kelas){
                1 -> tvClass.text = "ECONOMY"
                2 -> tvClass.text = "BUSSINESS"
            }
            when(type){
                1 -> tvType.text = "DEWASA"
                2 -> tvType.text = "ANAK-ANAK"
            }

            tvCode.text = code
            tvDestination.text = dest
            tvFrom.text = from
            tvAirlane.text = airlane
            tvDateAir.text = datesAir
            tvEstimated.text = estimatedAir
            tvChairs.text = chairs.toString()
            val pricesIdn = "Rp. ${price.toString()}"
            tvPrice.text = pricesIdn
            Glide.with(root).load("https://binarstudpenfinalprojectbe-production-77a5.up.railway.app$image").into(imageLogo)
        }
    }

    private fun deleteTicket(){
        binding.btnDelete.setOnClickListener {
            alertDelete()
        }
    }

    private fun alertDelete(){
        val alert  = AlertDialog.Builder(this).create()
        val views = AlertDeleteTicketBinding.inflate(layoutInflater)
        alert.setView(views.root)
        alert.window?.attributes?.windowAnimations = R.style.myDialogAnimation
        views.apply {
            btnCancel.setOnClickListener { alert.dismiss() }
            btnDelete.setOnClickListener { doDeleteTickets() }
        }
        alert.show()

    }

    private fun doDeleteTickets(){
        val id = intent.getStringExtra("id")
        authPreferencesViewModel.getToken().observe(this) { it ->
            if (it != null && it != "undefined") {
                if(id != null){
                    ticketViewModel.doDeleteTicket(it,id)
                    ticketViewModel.doDeleteTicketObserver().observe(this){ deleted ->
                       if(deleted != null){
                           if(deleted.deleted[0] > 0){
                               finish()
                           }
                       }
                    }
                }
            }
        }
    }

    private fun showLoading(show : Boolean){
        if(show){
            binding.apply {
                loading.visibility = View.VISIBLE
                loadingBg.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                loading.visibility = View.GONE
                loadingBg.visibility = View.GONE
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding =  null
    }


}