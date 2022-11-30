package binar.finalproject.MyAirFare.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import binar.finalproject.MyAirFare.databinding.ActivityPaymentDetailsBinding

class PaymentDetailsActivity : AppCompatActivity() {
    private var _binding : ActivityPaymentDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPaymentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        back()

    }

    private fun back(){
        binding.toolbar.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}