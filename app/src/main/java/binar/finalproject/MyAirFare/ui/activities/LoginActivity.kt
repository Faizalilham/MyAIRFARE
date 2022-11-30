package binar.finalproject.MyAirFare.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.databinding.ActivityLoginBinding
import binar.finalproject.MyAirFare.utils.AuthValidation
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel : AuthViewModel
    private lateinit var authPreferences : AuthPreferencesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        authPreferences = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        setContentView(binding.root)
        doRegister()
        doLogin()
        doResetPassword()
    }

    private fun doRegister(){
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

    private fun doResetPassword(){
        binding.tvForgetPassword.setOnClickListener {
            startActivity(Intent(this,ForgetPasswordActivity::class.java))
        }
    }

    private fun doLogin(){
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val validation = AuthValidation.loginValidation(email,password)
            if(validation == "success"){
                authViewModel.doLogin(email,password)
                authViewModel.doLoginObserver().observe(this){
                    if(it != null){
                        startActivity(Intent(this,MainActivity::class.java).also { its ->
                            Log.d("touken",it.token)
                            authPreferences.setToken(it.token)
                            finish()

                        })
                    }else{
                        Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, validation, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}