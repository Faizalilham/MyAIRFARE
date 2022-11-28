package binar.finalproject.MyAirFare.ui.fragments.onboarding.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.FragmentOnBoardingBinding
import com.example.githubapi.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class OnBoardingFragment : Fragment() {


    private var _binding : FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentOnBoardingBinding.inflate(layoutInflater)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPager()
    }

    private fun setViewPager(){
        val fragmentList = arrayListOf(
            FirstScreen(),
            SecondFragment(),
            ThirdScreen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.pageIndicator, binding.viewPager) { _, _ -> }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }




}