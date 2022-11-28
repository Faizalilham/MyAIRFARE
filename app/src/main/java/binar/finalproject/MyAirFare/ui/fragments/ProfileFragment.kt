package binar.finalproject.MyAirFare.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import binar.finalproject.MyAirFare.databinding.FragmentProfileBinding
import binar.finalproject.MyAirFare.ui.activities.LoginActivity


class ProfileFragment : Fragment() {


    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doLogin()

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