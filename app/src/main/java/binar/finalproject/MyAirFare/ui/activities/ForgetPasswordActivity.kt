package binar.finalproject.MyAirFare.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.databinding.ActivityForgetPasswordBinding
import binar.finalproject.MyAirFare.viewmodel.ResetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordActivity : AppCompatActivity() {
    private var _binding : ActivityForgetPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var resetPasswordViewModel: ResetPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        resetPasswordViewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]
        setContentView(binding.root)
        resetPassword()
    }

    private fun resetPassword(){
        binding.btnResetPassword.setOnClickListener {
            val email = binding.etEmail.text.toString()
            if(email.isNotEmpty()){
                if(email.contains("@")){
                    resetPasswordViewModel.doResetPassword(email)
                }else{
                    Toast.makeText(this, "Masukkan email yang valid", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            resetPasswordViewModel.doResetPasswordObserver().observe(this){
                if(it != null){
                    if(it.success == "Email reset password telah dikirim ke email yang dituju"){
                        startActivity(Intent(this,VerificationActivity::class.java))
                    }else{
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}