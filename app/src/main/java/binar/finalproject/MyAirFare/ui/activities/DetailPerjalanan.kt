package binar.finalproject.MyAirFare.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.ActivityDetailPerjalananBinding
import binar.finalproject.MyAirFare.databinding.AlertLoginBinding
import binar.finalproject.MyAirFare.model.tickets.Schedule
import binar.finalproject.MyAirFare.utils.DatePicker
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.waitlist.WaitListViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPerjalanan : AppCompatActivity() {
    private var _binding : ActivityDetailPerjalananBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var waitListViewModel : WaitListViewModel
    private var ticket_id = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailPerjalananBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        waitListViewModel = ViewModelProvider(this)[WaitListViewModel::class.java]
        setContentView(binding.root)
        back()
        setupView()
        doBooking()
        waitList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupView(){
        val i = intent.getParcelableExtra<Schedule>("schedule")
        if(i != null){
            binding.apply {
                ticket_id = i.id
                Glide.with(root).load("https://binarstudpenfinalprojectbe-production.up.railway.app${i.logo}").into(imageLogo)
                tvCode.text = i.flight_number
                tvFrom.text = i.from
                tvDestination.text = i.dest
                when(i.kelas){
                    1 -> tvClass.text = "ECONOMY"
                    2 -> tvClass.text = "BUSSINESS"
                }
                when(i.type){
                    1 -> tvType.text = "DEWASA"
                    2 -> tvType.text = "ANAK-ANAK"
                }
                val price = "Rp. ${i.price}"
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

    private fun doBooking(){
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
              binding.btnBooking.setOnClickListener {
                  val i = intent.getParcelableExtra<Schedule>("schedule")
                  startActivity(Intent(this,PaymentDetailsActivity::class.java).also{ its ->
                      its.putExtra("schedule",i)
                  })
              }
            }else{
                alertLogin()
            }
        }
    }

    private fun waitList(){
        binding.btnCart.setOnClickListener {
            postWaitList()
        }
    }

    private fun postWaitList() {
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}