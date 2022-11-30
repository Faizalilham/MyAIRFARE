package binar.finalproject.MyAirFare.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        val host = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(host.navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}