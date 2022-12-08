package binar.finalproject.MyAirFare_admin.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare_admin.databinding.ActivityDeleteUserBinding
import binar.finalproject.MyAirFare_admin.viewmodel.auth.AuthPreferencesViewModel
import binar.finalproject.MyAirFare_admin.viewmodel.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteUserActivity : AppCompatActivity() {
    private var _binding : ActivityDeleteUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDeleteUserBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        setContentView(binding.root)
        val i = intent.getStringExtra("ids")
        if(i != null){
            getTicketById(i)
        }
    }

    private fun getTicketById(i :String){
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
                userViewModel.getUserById(it,i)
                userViewModel.getUserByIdObserver().observe(this){ result ->
                    result?.user?.apply {
                        val tittle = result.user.username.split(".")
                        if(tittle.size > 1){
                            setupView(
                                tittle[0],
                                username,f_name,
                                l_name,email,password
                            )
                        }

                    }
                }
            }
        }
    }

    private fun setupView(tittle : String,username : String,
                         firstname:String,lastname : String,
                         email : String,password : String){

        binding.apply {
            tvTittle.setText(tittle)
            etUsername.setText(username)
            etFirstname.setText(firstname)
            etLastname.setText(lastname)
            etEmail.setText(email)
            etPassword.setText(password)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}