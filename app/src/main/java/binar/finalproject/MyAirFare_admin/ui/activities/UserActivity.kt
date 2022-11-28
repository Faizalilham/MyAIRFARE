package binar.finalproject.MyAirFare_admin.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import binar.finalproject.MyAirFare_admin.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private var _binding : ActivityUserBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}