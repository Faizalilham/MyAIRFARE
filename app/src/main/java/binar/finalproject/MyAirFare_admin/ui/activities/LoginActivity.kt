package binar.finalproject.MyAirFare_admin.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.utils.AuthValidation
import binar.finalproject.MyAirFare_admin.viewmodel.auth.AuthPreferencesViewModel
import binar.finalproject.MyAirFare_admin.viewmodel.auth.AuthViewModel
import binar.finalproject.MyAirFare_admin.MainActivity
import binar.finalproject.MyAirFare_admin.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        setContentView(binding.root)
        doLogin()
    }


    private fun doLogin(){
        binding.apply {
            btnLogin.setOnClickListener {
               val email = etEmail.text.toString()
               val password = etPassword.text.toString()
                val validation = AuthValidation.loginValidation(email,password)
                if(validation == "success"){
                    authViewModel.doLogin(email,password)
                    authViewModel.doLoginObserver().observe(this@LoginActivity){
                        if(it != null){
                            startActivity(Intent(this@LoginActivity,
                                MainActivity::class.java).also { _ ->
                                Log.d("touken",it.token)
                                Toast.makeText(this@LoginActivity, "Login success, hallo ${it.user.username}", Toast.LENGTH_SHORT).show()
                                authPreferencesViewModel.setToken(it.token)
                                finish()

                            })
                        }else{
                            authViewModel.messageObserver().observe(this@LoginActivity){ message ->
                                if(message != null){
                                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }else{
                    Toast.makeText(this@LoginActivity, validation, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}