package binar.finalproject.MyAirFare_admin.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import binar.finalproject.MyAirFare_admin.databinding.ActivityTicketAvailableBinding

class TicketAvailableActivity : AppCompatActivity() {
    private var _binding : ActivityTicketAvailableBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTicketAvailableBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}