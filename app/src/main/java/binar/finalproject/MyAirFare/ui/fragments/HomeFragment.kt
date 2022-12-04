package binar.finalproject.MyAirFare.ui.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.FragmentHomeBinding
import binar.finalproject.MyAirFare.ui.activities.SearchTicketActivity
import binar.finalproject.MyAirFare.utils.DatePicker
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(requireActivity())[AuthPreferencesViewModel::class.java]
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        search()
        setDate()
        setTime()
    }
    private fun setupView(){
        authPreferencesViewModel.getName().observe(requireActivity()){
               if(it != null && it != ""){
                val name = "Hallo $it"
                binding.tvHallo.text = name
            }
        }
    }
    private fun setDate(){
        binding.etDate.setOnClickListener {
            DatePicker.datePicker(requireActivity(),binding.etDate)
        }
    }

    private fun setTime(){
        binding.apply {
            etTime.setOnClickListener{
                DatePicker.timePicker(requireActivity(),childFragmentManager,etTime)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun search(){
        binding.apply {
            btnSearch.setOnClickListener {
                val from = etFrom.text.toString()
                val destination = etDestination.text.toString()
                val date = etDate.text.toString()
                val time = etTime.text.toString()
                val dates = DatePicker.formatterDate(date)
                if(from.isNotBlank() && destination.isNotBlank() && date.isNotBlank()  && time.isNotBlank()){
                    startActivity(Intent(requireActivity(),SearchTicketActivity::class.java).also{
                        it.putExtra("from",from)
                        it.putExtra("destination",destination)
                        it.putExtra("date",dates)
                        it.putExtra("time",time)

                    })
                }else{
                    Toast.makeText(requireActivity(), "Kolom asal dan tujuan wajib diisi", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}