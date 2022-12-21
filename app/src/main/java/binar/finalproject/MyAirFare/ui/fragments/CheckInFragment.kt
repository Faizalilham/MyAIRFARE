package binar.finalproject.MyAirFare.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.adapter.CheckInAdapter
import binar.finalproject.MyAirFare.databinding.FragmentActiveTicketBinding
import binar.finalproject.MyAirFare.model.room.CheckIn
import binar.finalproject.MyAirFare.viewmodel.checkin.CheckInRoomViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckInFragment : Fragment() {

    private lateinit var binding : FragmentActiveTicketBinding
    private lateinit var checkInRoomViewModel : CheckInRoomViewModel
    private lateinit var checkInAdapter: CheckInAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActiveTicketBinding.inflate(layoutInflater)
        checkInRoomViewModel = ViewModelProvider(this)[CheckInRoomViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllCheckIn()
    }

    private fun getAllCheckIn(){
        checkInRoomViewModel.getAllCheckIn()
        if(isAdded && activity != null){
            checkInRoomViewModel.getAllCheckInObserver().observe(requireActivity()){
                if(it != null){
                    setRecycler(it)
                }
            }
        }
    }

    private fun setRecycler(data : MutableList<CheckIn>){
        checkInAdapter = CheckInAdapter(object : CheckInAdapter.OnClick{
            override fun onDelete(checkIn: CheckIn) {
                checkInRoomViewModel.deleteCheckIn(checkIn)
                if(isAdded && activity != null){
                    checkInRoomViewModel.deleteCheckInObserver().observe(requireActivity()){
                        if(it != null){
                            Navigation.findNavController(binding.root).navigate(R.id.checkInFragment)
                            Toast.makeText(requireActivity(), "Data CheckIn Terhapus", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

        })
        checkInAdapter.submitData(data)
        binding.ticketRecycler.apply {
            adapter = checkInAdapter
            if(isAdded && activity != null){
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }

    }

}