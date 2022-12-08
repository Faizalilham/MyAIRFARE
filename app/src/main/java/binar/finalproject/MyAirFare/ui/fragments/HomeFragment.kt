package binar.finalproject.MyAirFare.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.MyAirFare.adapter.TicketAdapter
import binar.finalproject.MyAirFare.databinding.FragmentHomeBinding
import binar.finalproject.MyAirFare.model.tickets.Schedule
import binar.finalproject.MyAirFare.ui.activities.DetailPerjalanan
import binar.finalproject.MyAirFare.ui.activities.SearchTicketActivity
import binar.finalproject.MyAirFare.utils.DatePicker
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.ticket.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var ticketAdapter: TicketAdapter
    private lateinit var ticketViewModel: TicketViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(requireActivity())[AuthPreferencesViewModel::class.java]
        ticketViewModel = ViewModelProvider(requireActivity())[TicketViewModel::class.java]
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        search()
        setDate()
        setTime()
        getAllTicket()
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

    private fun getAllTicket(){
        authPreferencesViewModel.getToken().observe(requireActivity()){
            if(it != null && it != ""){
                ticketViewModel.readTicket(it)
                showLoading(true)
                ticketViewModel.readTicketObserver().observe(requireActivity()){list ->
                   if(list != null){
                       showLoading(false)
                       setRecycler(list)
                   }
                }
            }
        }
    }


    private fun setRecycler(data : MutableList<Schedule>){
        ticketAdapter = TicketAdapter(object : TicketAdapter.OnClick{
            override fun onClicked(schedule: Schedule) {
                startActivity(Intent(requireActivity(),DetailPerjalanan::class.java).also{
                    it.putExtra("schedule",schedule)
                })
            }

        })
        ticketAdapter.submitData(data)
        binding.ticketRecycler.apply {
            adapter = ticketAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun showLoading(show : Boolean){
        if(show) binding.loading.visibility = View.VISIBLE else  binding.loading.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}