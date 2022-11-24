package binar.finalproject.MyAirFare.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.FragmentCartBinding
import binar.finalproject.MyAirFare.databinding.FragmentInActiveTicketBinding


class InActiveTicketFragment : Fragment() {


    private var _binding : FragmentInActiveTicketBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInActiveTicketBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }




}