package binar.finalproject.MyAirFare.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import binar.finalproject.MyAirFare.databinding.FragmentInActiveTicketBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }




}