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
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.checkin.CheckInRoomViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckInFragment : Fragment() {

    private lateinit var binding : FragmentActiveTicketBinding
    private lateinit var checkInRoomViewModel : CheckInRoomViewModel
    private lateinit var checkInAdapter: CheckInAdapter
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActiveTicketBinding.inflate(layoutInflater)
        checkInRoomViewModel = ViewModelProvider(this)[CheckInRoomViewModel::class.java]
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkGuest()
    }

    private fun checkGuest(){
        authPreferencesViewModel.getToken().observe(requireActivity()){
            if(it != null && it != "undefined"){
                getAllCheckIn()
            }else{
                showWarning(true)
            }
        }
    }

    private fun getAllCheckIn(){
        checkInRoomViewModel.getAllCheckIn()
        showLoading(true)
        if(isAdded && activity != null){
            checkInRoomViewModel.getAllCheckInObserver().observe(requireActivity()){
                if(it != null){
                   if(it.size > 0){
                       showLoading(false)
                       showWarning(false)
                       setRecycler(it)
                   }else{
                       showLoading(false)
                       showWarning(true)
                   }
                }else{
                    showLoading(false)
                    showWarning(true)
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
                            Navigation.findNavController(binding.root).navigate(R.id.ticketFragment)
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

    private fun showLoading(loading: Boolean){
        if(loading) binding.loading.visibility = View.VISIBLE else binding.loading.visibility = View.GONE
    }

    private fun showWarning(show : Boolean){
        binding.apply {
            if(show){
                imageNotFound.visibility = View.VISIBLE
                tvNotFound.visibility = View.VISIBLE
                tvNotFound.text = "Tidak ada data checkin"
            }else{
                imageNotFound.visibility = View.GONE
                tvNotFound.visibility = View.GONE
            }
        }
    }

}