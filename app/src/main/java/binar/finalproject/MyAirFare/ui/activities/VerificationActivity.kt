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
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun goToEmail(){
        binding.btnEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("mailto :")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }else{
                Toast.makeText(this, "Aplikasi email belum terinstall", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}