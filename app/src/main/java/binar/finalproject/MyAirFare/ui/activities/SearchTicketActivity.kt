package binar.finalproject.MyAirFare.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import binar.finalproject.MyAirFare.databinding.ActivitySearchTicketBinding

class SearchTicketActivity : AppCompatActivity() {
    private var _binding : ActivitySearchTicketBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView(){
        val from = intent.getStringExtra("from")
        val dest = intent.getStringExtra("destination")
        if(from != null && dest != null){
            binding.apply {
                etKeberangkatan.setText(from)
                etTiba.setText(dest)
            }
        }
    }

    private fun doSearch(){

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}