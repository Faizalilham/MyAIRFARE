package binar.finalproject.MyAirFare.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import binar.finalproject.MyAirFare.databinding.ActivityVerifikasiBinding

class VerificationActivity : AppCompatActivity() {
    private var _binding : ActivityVerifikasiBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVerifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        goToEmail()
        getEmailName()
    }

    private fun getEmailName(){
        val intent = intent.getStringExtra("email")
        if(intent != null){
           binding.tvEmail.text = intent
        }
    }

    private fun goToEmail(){
        binding.btnEmail.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.setPackage("com.google.android.gm")
//            startActivity(intent)
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_APP_EMAIL)
            startActivity(intent)

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}