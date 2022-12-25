package binar.finalproject.MyAirFare.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.AlertLogoutBinding
import binar.finalproject.MyAirFare.databinding.FragmentProfileBinding
import binar.finalproject.MyAirFare.model.user.CurrentUser
import binar.finalproject.MyAirFare.ui.activities.EditProfileActivity
import binar.finalproject.MyAirFare.ui.activities.LoginActivity
import binar.finalproject.MyAirFare.ui.activities.SettingActivity
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.CurrentUserViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var currentUserViewModel: CurrentUserViewModel
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        currentUserViewModel = ViewModelProvider(this)[CurrentUserViewModel::class.java]
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(
            R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(),gso)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doLogin()
        getCurrentUser()
        doLogout()
        goToSetting()

    }

    private fun getCurrentUser(){
        authPreferencesViewModel.getToken().observe(requireActivity()){
            Log.d("token", it)
            if(it != null){
                setupCurrentUser(it)
            }
        }
    }


    @SuppressLint("ResourceAsColor")
    private fun setupCurrentUser(token : String){
        currentUserViewModel.currentUser(token)
        binding.loadingBg.visibility = View.VISIBLE
        showLoading(true)
        currentUserViewModel.currentUserObserver().observe(requireActivity()){
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
                binding.loadingBg.visibility = View.GONE
            }
        }

    }

    private fun goToDetail(user : CurrentUser){
        binding.cardDetailProfile.setOnClickListener {
            startActivity(Intent(requireActivity(),EditProfileActivity::class.java).also {
                it.putExtra("user",user)
            })
        }
    }

    private fun goToSetting(){
        binding.cardPengaturan.setOnClickListener {
            startActivity(Intent(requireActivity(),SettingActivity::class.java))
        }
    }

    private fun doLogout(){
        binding.cardKeluar.setOnClickListener {
           alertLogout()
        }
    }

    private fun showLoading(show : Boolean){
        if(show) binding.loading.visibility = View.VISIBLE else  binding.loading.visibility = View.GONE
    }

    private fun setupView(username : String,email : String,image : String){
        binding.apply {
            showLoading(false)
            binding.loadingBg.visibility = View.GONE
            tvUsername.visibility = View.VISIBLE
            tvEmail.visibility = View.VISIBLE
            cardDetailProfile.visibility = View.VISIBLE
            cardPengaturan.visibility = View.GONE
            cardKeluar.visibility = View.VISIBLE
            tvBeforeLogin.visibility = View.GONE
            btnLogin.visibility = View.GONE
            tvUsername.text = username
            tvEmail.text = email
            activity?.let { Glide.with(it).load("https://binarstudpenfinalprojectbe-production-77a5.up.railway.app$image").into(imageProfile) }
        }
    }

    private fun alertLogout(){
        val alert = AlertDialog.Builder(requireContext()).create()
        val view =  AlertLogoutBinding.inflate(layoutInflater)
        alert.setView(view.root)
        alert.window?.attributes?.windowAnimations = R.style.myDialogAnimation
        view.apply {
            btnLogout.setOnClickListener {
                auth.signOut()
                googleSignInClient.signOut()
                authPreferencesViewModel.deleteToken()
                binding.apply {
                    tvUsername.visibility = View.GONE
                    tvEmail.visibility = View.GONE
                    cardDetailProfile.visibility = View.GONE
                    cardPengaturan.visibility = View.GONE
                    cardKeluar.visibility = View.GONE
                    tvBeforeLogin.visibility = View.VISIBLE
                    btnLogin.visibility = View.VISIBLE
                }
                Navigation.findNavController(binding.root).navigate(R.id.profileFragment)
                alert.dismiss()
            }
            btnCancel.setOnClickListener {
                alert.dismiss()
            }
        }
        alert.show()
    }

    private fun doLogin(){
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(activity,LoginActivity::class.java))
        }
    }

}