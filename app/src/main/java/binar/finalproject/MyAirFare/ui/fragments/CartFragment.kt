package binar.finalproject.MyAirFare.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.databinding.FragmentCartBinding
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {


    private var _binding : FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView(){
        authPreferencesViewModel.getToken().observe(requireActivity()){
            if(it != null && it != "undefined"){
                nonGuest()
            }else{
                guest()
            }
        }
    }

    private fun guest(){
        binding.apply {
            imageNotFound.visibility = View.GONE
            tvNotFound.visibility = View.GONE
            recyclerCart.visibility = View.GONE
            imageGuest.visibility = View.VISIBLE
            tvGuest.visibility = View.VISIBLE
            tvGuest2.visibility = View.VISIBLE
            btnLogin.visibility = View.VISIBLE
        }
    }

    private fun nonGuest(){
        binding.apply {
            imageNotFound.visibility = View.VISIBLE
            tvNotFound.visibility = View.VISIBLE
            recyclerCart.visibility = View.VISIBLE
            imageGuest.visibility = View.GONE
            tvGuest.visibility = View.GONE
            tvGuest2.visibility = View.GONE
            btnLogin.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}