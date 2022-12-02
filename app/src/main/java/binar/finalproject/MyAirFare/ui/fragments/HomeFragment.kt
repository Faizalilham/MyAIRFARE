package binar.finalproject.MyAirFare.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import binar.finalproject.MyAirFare.databinding.FragmentHomeBinding
import binar.finalproject.MyAirFare.ui.activities.SearchTicketActivity
import binar.finalproject.MyAirFare.utils.DatePicker


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
        search()
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

    private fun search(){
        binding.apply {
            val from = etDari.text.toString()
            val destination = etKe.text.toString()
            if(from.isNotBlank() && destination.isNotBlank()){
                btnSearch.setOnClickListener {
                    startActivity(Intent(requireActivity(),SearchTicketActivity::class.java).also{
                        it.putExtra("from",from)
                        it.putExtra("destination",destination)
                    })
                }
            }else{
                Toast.makeText(requireActivity(), "Kolom asal dan tujuan wajib diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}