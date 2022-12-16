package binar.finalproject.MyAirFare.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import binar.finalproject.MyAirFare.databinding.FragmentTicketBinding
import binar.finalproject.MyAirFare.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class TicketFragment : Fragment() {


    private var _binding : FragmentTicketBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPager()
    }

    private fun setViewPager(){
        val fragmentList = arrayListOf(
            WaitingPaymentFragment(),
            ActiveTicketFragment(),
            InActiveTicketFragment()
        )
        val viewPagerAdapter = ViewPagerAdapter(fragmentList,childFragmentManager,lifecycle)
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabsLayout,binding.viewPager){tab,position ->
            when(position){
                0 -> tab.text = "Menunggu Pembayaran"
                1 -> tab.text = "Aktif"
                2 -> tab.text = "Non Aktif"

            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}