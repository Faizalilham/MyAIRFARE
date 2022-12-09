package binar.finalproject.MyAirFare_admin.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.MyAirFare_admin.adapter.TicketAdapter
import binar.finalproject.MyAirFare_admin.databinding.ActivityTicketAvailableBinding
import binar.finalproject.MyAirFare_admin.model.ticket.Tickets
import binar.finalproject.MyAirFare_admin.utils.TicketConstant
import binar.finalproject.MyAirFare_admin.viewmodel.auth.AuthPreferencesViewModel
import binar.finalproject.MyAirFare_admin.viewmodel.ticket.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketAvailableActivity : AppCompatActivity() {
    private var _binding : ActivityTicketAvailableBinding? = null
    private val binding get() = _binding!!
    private lateinit var ticketViewModel : TicketViewModel
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var ticketAdapter: TicketAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTicketAvailableBinding.inflate(layoutInflater)
        ticketViewModel = ViewModelProvider(this)[TicketViewModel::class.java]
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        setContentView(binding.root)
        readAllTicket()
    }

    private fun readAllTicket(){
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
                ticketViewModel.readTicket(it)
                showLoading(true)
                ticketViewModel.readTicketObserver().observe(this){ listTicket ->
                    if(listTicket != null){
                        showLoading(false)
                        setupRecycler(listTicket)
                    }else{
                        ticketViewModel.messageObserver().observe(this){ message ->
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecycler(datas : MutableList<Tickets>){
        ticketAdapter = TicketAdapter(object : TicketAdapter.OnClick{
            override fun onDetail(ticket: Tickets) {
                startActivity(Intent(this@TicketAvailableActivity,DetailTicketActivity::class.java).also{
                    it.putExtra("id",ticket.id)
                })
            }

            override fun onUpdate(ticket: Tickets) {
                startActivity(Intent(this@TicketAvailableActivity,AddTicketActivity::class.java).also{
                    it.putExtra("id",ticket.id)
                    it.putExtra("types",TicketConstant.UPDATE)

                })
            }


        })
        ticketAdapter.submitData(datas)
        binding.recyclerTicket.apply {
            adapter = ticketAdapter
            layoutManager = LinearLayoutManager(this@TicketAvailableActivity)
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