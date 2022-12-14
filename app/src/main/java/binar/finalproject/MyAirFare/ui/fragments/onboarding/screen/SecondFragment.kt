package binar.finalproject.MyAirFare.ui.fragments.onboarding.screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.FragmentSecondBinding
import binar.finalproject.MyAirFare.ui.activities.MainActivity
import binar.finalproject.MyAirFare.viewmodel.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SecondFragment : Fragment() {

    private var _binding : FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var onBoarding : OnBoardingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(layoutInflater)
        onBoarding = ViewModelProvider(requireActivity())[OnBoardingViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        next()
        back()
        skip()
    }
    private fun next(){
        val viewPager =  activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.next.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }

    private fun back(){
        val viewPager =  activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.back.setOnClickListener {
            viewPager?.currentItem = 0
        }
    }

    private fun skip(){
        binding.skip.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java).also{
                onBoarding.setBoardingKey(true)
                activity?.finish()
            })
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}