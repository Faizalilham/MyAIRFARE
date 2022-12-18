package binar.finalproject.MyAirFare_admin.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare_admin.R
import binar.finalproject.MyAirFare_admin.databinding.ActivityDeleteUserBinding
import binar.finalproject.MyAirFare_admin.databinding.AlertDeleteUserBinding
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
            binding.btnDelete.setOnClickListener { deletedUser(i) }
        }
        back()
    }
    private fun back(){
        binding.toolbar.setOnClickListener { finish() }
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

    private fun deleteUser(id : String){
        binding.btnDelete.setOnClickListener {
            alertDelete(id )
        }
    }

    private fun deletedUser(id : String){
        authPreferencesViewModel.getToken().observe(this){
            if(it != null){
                userViewModel.doDeleteUser(it,id)
                userViewModel.doDeleteUserObserver().observe(this){response ->
                    if(response != null){
                        if(response.deleted.size > 0){
                            finish()
                            Toast.makeText(this, "Hapus Akun Berhasil", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        userViewModel.messageObserver().observe(this){message ->
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }

    private fun alertDelete(id : String){
        val alert = AlertDialog.Builder(this).create()
        val view = AlertDeleteUserBinding.inflate(layoutInflater)
        alert.setView(view.root)
        alert.window?.attributes?.windowAnimations = R.style.myDialogAnimation
        view.apply {
            btnYes.setOnClickListener { deletedUser(id) }
            btnCancel.setOnClickListener { alert.dismiss() }
        }
        alert.show()
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