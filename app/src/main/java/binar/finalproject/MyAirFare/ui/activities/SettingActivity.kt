package binar.finalproject.MyAirFare.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import binar.finalproject.MyAirFare.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private var _binding : ActivitySettingBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}