package binar.finalproject.MyAirFare_admin.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.model.user.CurrentUser
import binar.finalproject.MyAirFare_admin.databinding.ActivityProfileAdminBinding
import binar.finalproject.MyAirFare_admin.viewmodel.auth.AuthPreferencesViewModel
import binar.finalproject.MyAirFare_admin.viewmodel.user.CurrentUserViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileAdminActivity : AppCompatActivity() {
    private var _binding : ActivityProfileAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUserViewModel: CurrentUserViewModel
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileAdminBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        currentUserViewModel = ViewModelProvider(this)[CurrentUserViewModel::class.java]
        setContentView(binding.root)
        getCurrentUser()
        doLogout()

    }

    private fun getCurrentUser(){
        authPreferencesViewModel.getToken().observe(this){
            Log.d("token", it)
            if(it != null){
                setupCurrentUser(it)
            }
        }
    }


    private fun setupCurrentUser(token : String){
        currentUserViewModel.currentUser(token)
//        binding.profileLinear.setBackgroundColor(R.color.transparents)
        showLoading(true)
        currentUserViewModel.currentUserObserver().observe(this){
            Log.d("CurrentUser",it.toString())
            if(it != null){
                setupView(
                    it.user.username,
                    it.user.email,
                    it.user.photo
                )
                Log.d("CEKK CURRENT IMAGE",it.user.photo)
                goToDetail(it)

            }else{
                showLoading(false)
            }
        }

    }

    private fun goToDetail(user : CurrentUser){
        binding.cardDetailProfile.setOnClickListener {
            startActivity(Intent(this,EditProfileAdminActivity::class.java).also {
                it.putExtra("user",user)
            })
        }
    }


    private fun doLogout(){
        binding.cardKeluar.setOnClickListener {
           startActivity(Intent(this,LoginActivity::class.java).also{
               authPreferencesViewModel.deleteToken()
           })

        }
    }

    private fun showLoading(show : Boolean){
        if(show) binding.loading.visibility = View.VISIBLE else  binding.loading.visibility = View.GONE
    }

    private fun setupView(username : String,email : String,image : String){
        binding.apply {
            showLoading(false)
            tvUsername.text = username
            tvEmail.text = email
            Glide.with(this@ProfileAdminActivity).load("https://binarstudpenfinalprojectbe-production.up.railway.app$image").into(imageProfile)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}