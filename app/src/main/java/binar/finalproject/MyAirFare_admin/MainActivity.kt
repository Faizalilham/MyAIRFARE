package binar.finalproject.MyAirFare_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import binar.finalproject.MyAirFare_admin.databinding.ActivityMainBinding
import binar.finalproject.MyAirFare_admin.ui.activities.*


class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

    }

    private fun setupView(){
        binding.apply {
            cardDaftarPengguna.setOnClickListener { intents(UserActivity::class.java) }
            cardTambahTiket.setOnClickListener { intents(AddTicketActivity::class.java) }
            cardTiketTerjual.setOnClickListener { intents(SoldTicketActivity::class.java) }
            cardTiketTersedia.setOnClickListener { intents(TicketAvailableActivity::class.java) }
            imageAdmin.setOnClickListener { intents(EditProfileAdminActivity::class.java) }
        }
    }

    private fun <T> intents(modelClass : Class<T>){
        startActivity(Intent(this,modelClass))
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}