package binar.finalproject.MyAirFare.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import binar.finalproject.MyAirFare.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private var _binding : ActivitySplashScreenBinding? = null
    private val binding  get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSplashScreen()
    }

    private fun setSplashScreen(){
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(Intent(this,MainActivity::class.java).also{finish()})
        },1500)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}