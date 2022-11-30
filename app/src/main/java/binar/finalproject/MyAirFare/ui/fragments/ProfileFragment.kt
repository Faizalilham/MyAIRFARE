package binar.finalproject.MyAirFare.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.databinding.FragmentProfileBinding
import binar.finalproject.MyAirFare.ui.activities.EditProfileActivity
import binar.finalproject.MyAirFare.ui.activities.LoginActivity
import binar.finalproject.MyAirFare.ui.activities.SettingActivity
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.CurrentUserViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {


    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUserViewModel: CurrentUserViewModel
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        currentUserViewModel = ViewModelProvider(this)[CurrentUserViewModel::class.java]
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
            }else{
                Toast.makeText(requireActivity(), "null", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCurrentUser(token : String){
        currentUserViewModel.currentUser(token)
        currentUserViewModel.currentUserObserver().observe(requireActivity()){
            Log.d("CurrentUser",it.toString())
            if(it != null){

                setupView(
                    it.username,
                    it.email,
                    it.photo
                )
            }
        }
        goToDetail(token)
    }

    private fun goToDetail(token : String){
        binding.cardDetailProfile.setOnClickListener {
            startActivity(Intent(requireActivity(),EditProfileActivity::class.java).also {
                it.putExtra("token",token)
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
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false)
            }
            ft.detach(this).attach(this).commit()

        }
    }



    private fun setupView(username : String,email : String,image : String){
        binding.apply {
            tvUsername.visibility = View.VISIBLE
            tvEmail.visibility = View.VISIBLE
            cardDetailProfile.visibility = View.VISIBLE
            cardPengaturan.visibility = View.VISIBLE
            cardKeluar.visibility = View.VISIBLE
            tvBeforeLogin.visibility = View.GONE
            btnLogin.visibility = View.GONE
            tvUsername.text = username
            tvEmail.text = email
            Glide.with(requireActivity()).load(image).into(imageProfile)
        }
    }

    private fun doLogin(){
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(activity,LoginActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}