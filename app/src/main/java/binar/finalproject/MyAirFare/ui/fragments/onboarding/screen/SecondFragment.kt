package binar.finalproject.MyAirFare.ui.fragments.onboarding.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.FragmentFirstScreenBinding
import binar.finalproject.MyAirFare.databinding.FragmentSecondBinding


class SecondFragment : Fragment() {

    private var _binding : FragmentSecondBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager =  activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.next.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}