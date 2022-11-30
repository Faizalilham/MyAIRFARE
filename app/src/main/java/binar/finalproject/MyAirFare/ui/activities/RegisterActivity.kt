package binar.finalproject.MyAirFare.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.ActivityRegisterBinding
import binar.finalproject.MyAirFare.utils.AuthValidation
import binar.finalproject.MyAirFare.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private var _binding : ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel : AuthViewModel
    private var tittle = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        setContentView(binding.root)
        doLogin()
        dropDownMenu()
        doRegister()
    }

    private fun doLogin(){
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }


    private fun dropDownMenu(){
        val data = resources.getStringArray(R.array.tittle_item)
        val adapter = ArrayAdapter(this,R.layout.dropdown_tittle_item,data)
        with(binding.tvTittle){
            setAdapter(adapter)
            onItemClickListener = this@RegisterActivity
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        tittle = item
    }

    private fun doRegister(){
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val firstname = binding.etFirstname.text.toString()
            val lastname = binding.etLastname.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            val validation = AuthValidation.registerValidation(username,firstname,lastname,email,password,confirmPassword, tittle)
            if(validation == "success"){
               authViewModel.doRegister(username,firstname,lastname,email,password,confirmPassword,tittle)
               authViewModel.doRegisterObserver().observe(this){
                   if(it != null){
                       Toast.makeText(this, "Berhasil Daftar", Toast.LENGTH_SHORT).show()
                       startActivity(Intent(this,VerificationActivity::class.java))
                   }else{
                       Toast.makeText(this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
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