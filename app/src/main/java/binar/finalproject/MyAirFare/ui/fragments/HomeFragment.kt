package binar.finalproject.MyAirFare.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import binar.finalproject.MyAirFare.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {


    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }
    private fun setupView(){
        binding.switchPulangPergi.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.ilTiba.visibility = View.VISIBLE
            }else{
                binding.ilTiba.visibility = View.GONE
            }
            binding.switchPulangPergi.isChecked = isChecked
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}