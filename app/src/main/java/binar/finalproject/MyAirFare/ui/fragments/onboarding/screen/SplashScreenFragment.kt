package binar.finalproject.MyAirFare.ui.fragments.onboarding.screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.FragmentSplashScreenBinding
import binar.finalproject.MyAirFare.ui.activities.MainActivity
import binar.finalproject.MyAirFare.viewmodel.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private var _binding : FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var onBoarding : OnBoardingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        onBoarding = ViewModelProvider(requireActivity())[OnBoardingViewModel::class.java]
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBoarding.getBoardingKey().observe(requireActivity()){
            if(it != null){
                Handler().postDelayed({
                    if(it){
                        if (isAdded && activity != null) {
                            startActivity(Intent(requireActivity(),MainActivity::class.java).also {
                                requireActivity().finish()
                            })
                        }
                    }else{
                        findNavController().navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
//                        startActivity(Intent(activity, SplashScreen::class.java).also { activity?.finish() })
                    }
                },2000)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }




}