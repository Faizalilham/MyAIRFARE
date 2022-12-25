package binar.finalproject.MyAirFare.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.adapter.CartAdapter
import binar.finalproject.MyAirFare.databinding.FragmentCartBinding
import binar.finalproject.MyAirFare.model.tickets.Schedule
import binar.finalproject.MyAirFare.model.wait_list.TicketWaitList
import binar.finalproject.MyAirFare.model.wait_list.WaitList
import binar.finalproject.MyAirFare.ui.activities.DetailPerjalanan
import binar.finalproject.MyAirFare.ui.activities.LoginActivity
import binar.finalproject.MyAirFare.ui.activities.MainActivity
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.waitlist.WaitListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding : FragmentCartBinding
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var waitListViewModel : WaitListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        waitListViewModel = ViewModelProvider(this)[WaitListViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        getTicketWaitList()
    }


    private fun getTicketWaitList(){
        authPreferencesViewModel.getToken().observe(requireActivity()){
            if(it != null && it != "undefined"){
                waitListViewModel.getAllWaitList(it)
                showLoading(true)
                waitListViewModel.getAllWaitListObserver().observe(requireActivity()){ waitList ->
                    Log.d("WAITLIST","$waitList")
                    if(waitList != null){
                        showLoading(false)
                        if(waitList.size > 0){
                            binding.imageNotFound.visibility = View.GONE
                            binding.tvNotFound.visibility = View.GONE
                            setRecycler(waitList)
                        }else{
                            binding.imageNotFound.visibility = View.VISIBLE
                            binding.tvNotFound.visibility = View.VISIBLE
                        }
                    }else{
                        waitListViewModel.messageObserver().observe(requireActivity()){ message ->
                            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun deleteWaitList(id : Int){
        authPreferencesViewModel.getToken().observe(requireActivity()){
            if(it != null && it != "undefined"){
                Log.d("TOKEN ID","$it, $id")
                waitListViewModel.deleteWaitList(it,id)
                waitListViewModel.deleteWaitListObserver().observe(requireActivity()){ waitList ->
                    if(waitList != null && waitList.deleted == "Data have deleted successfully"){
                        Toast.makeText(requireActivity(), "Berhasil Hapus Keranjang", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(binding.root).navigate(R.id.cartFragment)
                    }else{
                        waitListViewModel.messageObserver().observe(requireActivity()){ message ->
                            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setRecycler(data : MutableList<WaitList>){
        cartAdapter = CartAdapter(object : CartAdapter.OnClickListener{
            override fun onDelete(id : Int) {
                deleteWaitList(id)
            }

            override fun onDetail(schedule: MutableList<Schedule>, chairs: MutableList<Int>,waitListId : String) {
                startActivity(Intent(requireActivity(), DetailPerjalanan::class.java).also{
                    it.putExtra("schedule",schedule[0])
                    if(schedule.size > 1){
                        it.putExtra("returnFlight",schedule[1])
                    }
                    it.putIntegerArrayListExtra("chairs", ArrayList(chairs))
                    it.putExtra("waitList",true)
                    it.putExtra("waitListId",waitListId)
                })
            }
        })
        cartAdapter.submiData(data)
        binding.recyclerCart.apply {
            adapter = cartAdapter
            if (isAdded && activity != null) {
                layoutManager = LinearLayoutManager(requireActivity())
            }

        }
    }

    private fun setupView(){
        authPreferencesViewModel.getToken().observe(requireActivity()){
            if(it != null && it != "undefined"){
                showWarning(true)
            }else{
                showWarning(false)
            }
        }
    }

    private fun showLoading(loading: Boolean){
        if(loading) binding.loading.visibility = View.VISIBLE else binding.loading.visibility = View.GONE
    }

    private fun doLogin(){
        binding.btnLogin.setOnClickListener { startActivity(Intent(requireActivity(),LoginActivity::class.java))}
    }


    private fun showWarning(show : Boolean){
        binding.apply {
            if(show){
                recyclerCart.visibility = View.VISIBLE
                imageGuest.visibility = View.GONE
                tvGuest.visibility = View.GONE
                tvGuest2.visibility = View.GONE
                btnLogin.visibility = View.GONE
            }else{
                recyclerCart.visibility = View.GONE
                imageGuest.visibility = View.VISIBLE
                tvGuest.visibility = View.VISIBLE
                tvGuest2.visibility = View.VISIBLE
                btnLogin.visibility = View.VISIBLE
                doLogin()
            }
        }
    }





}