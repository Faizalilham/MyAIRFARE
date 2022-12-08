package binar.finalproject.MyAirFare.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.ActivityDetailPerjalananBinding
import binar.finalproject.MyAirFare.databinding.AlertLoginBinding
import binar.finalproject.MyAirFare.model.tickets.Schedule
import binar.finalproject.MyAirFare.utils.DatePicker
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPerjalanan : AppCompatActivity() {
    private var _binding : ActivityDetailPerjalananBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailPerjalananBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        setContentView(binding.root)
        back()
        setupView()
        doBooking()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupView(){
        val i = intent.getParcelableExtra<Schedule>("schedule")
        if(i != null){
            binding.apply {
                Glide.with(root).load("https://binarstudpenfinalprojectbe-production.up.railway.app${i.logo}").into(imageLogo)
                tvKodePenerbangan.text = i.flight_number
                tvKotaAsal.text = i.from
                tvKotaTujuan.text = i.dest
                tvPrice.text = i.price.toString()
                tvJamAsal.text = DatePicker.timeCalculation(i.date_air)
                tvJamTujuan.text = DatePicker.timeCalculation(i.estimated_up_dest)
                tvTanggalAsal.text = DatePicker.dateCalculation(i.date_air)
                tvTanggalTujuan.text = DatePicker.dateCalculation(i.estimated_up_dest)

            }
        }
    }

    private fun doBooking(){
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
              binding.btnBooking.setOnClickListener {
                  startActivity(Intent(this,PaymentDetailsActivity::class.java).also{ its ->
                      its.putExtra("kode",binding.tvKodePenerbangan.text.toString())
                  })
              }
            }else{
                alertLogin()
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
        binding.toolbar.setOnClickListener { finish() }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}