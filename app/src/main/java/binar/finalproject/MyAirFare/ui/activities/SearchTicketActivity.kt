@file:Suppress("UsePropertyAccessSyntax", "UsePropertyAccessSyntax")

package binar.finalproject.MyAirFare.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.MyAirFare.adapter.TicketAdapter
import binar.finalproject.MyAirFare.databinding.ActivitySearchTicketBinding
import binar.finalproject.MyAirFare.model.tickets.Schedule
import binar.finalproject.MyAirFare.viewmodel.ticket.FilterTicketViewModel
import binar.finalproject.MyAirFare.viewmodel.ticket.SearchTicketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchTicketActivity : AppCompatActivity() {
    private var _binding : ActivitySearchTicketBinding? = null
    private val binding get() = _binding!!
    private lateinit var ticketAdapter: TicketAdapter
    private lateinit var searchTicketViewModel: SearchTicketViewModel
    private lateinit var filterTicketViewModel: FilterTicketViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchTicketBinding.inflate(layoutInflater)
        searchTicketViewModel = ViewModelProvider(this)[SearchTicketViewModel::class.java]
        filterTicketViewModel = ViewModelProvider(this)[FilterTicketViewModel::class.java]
        setContentView(binding.root)
        setupView()
        back()

    }

    private fun back(){
        binding.toolbar.setOnClickListener { finish() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupView(){
        val from = intent.getStringExtra("from")
        val dest = intent.getStringExtra("destination")
        val date =  intent.getStringExtra("date")
        val kelas =  intent.getStringExtra("kelas")
        val tipe =  intent.getStringExtra("tipe")
        val dateReturn =  intent.getStringExtra("dateReturn")
        binding.apply {
            etKeberangkatan.setText(from)
            etTiba.setText(dest)
        }
        if(from != null && dest != null && date != null && kelas != null && tipe != null  && dateReturn != null){
            doSearchTicket(from,dest,date,kelas,tipe,dateReturn)
        }else if(from != null && dest != null && date != null){
            doSearchSchedule(from,dest,date)
        }

    }

    private fun doSearchTicket(from : String, dest : String,date :String,kelas : String,tipe : String,dateReturn : String){
        filterTicketViewModel.doFilterTicket(from,dest,date,kelas,tipe, dateReturn)
        showLoading(true)
        filterTicketViewModel.doFilterTicketObserver().observe(this){
            if(it != null){
                showLoading(false)
                if(it.tickets.go.size > 0 && it.tickets.return_flight.size > 0 ){
                    it.tickets.go.forEach { go ->
                        it.tickets.return_flight.forEach { rtn ->
                            if(go.available.size > 0 && rtn.available.size > 0){
                                binding.apply {
                                    imageNotFound.visibility = View.GONE
                                    tvNotFound.visibility = View.GONE
                                }
                                setupRecycler(it.tickets.go,it.tickets.return_flight)
                            }else{
                                binding.apply {
                                    imageNotFound.visibility = View.VISIBLE
                                    tvNotFound.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }else{
                    binding.apply {
                        imageNotFound.visibility = View.VISIBLE
                        tvNotFound.visibility = View.VISIBLE
                    }
                }
            }else{
                searchTicketViewModel.messageObserver().observe(this){message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doSearchSchedule(from : String, dest : String,dateTime :String){
        searchTicketViewModel.doSearchTicket(from,dest,dateTime)
        showLoading(true)
        searchTicketViewModel.doSearchTicketObserver().observe(this){
            if(it != null){
                showLoading(false)
                if(it.tickets.go.size > 0){
                    it.tickets.go.forEach { go ->
                        if(go.available.size > 0 ){
                            binding.apply {
                                imageNotFound.visibility = View.GONE
                                tvNotFound.visibility = View.GONE
                            }
                            setupRecycler(it.tickets.go, mutableListOf())
                        }else{
                            binding.apply {
                                imageNotFound.visibility = View.VISIBLE
                                tvNotFound.visibility = View.VISIBLE
                            }
                        }
                    }
                }else{
                    binding.apply {
                        imageNotFound.visibility = View.VISIBLE
                        tvNotFound.visibility = View.VISIBLE
                    }
                }
            }else{
                searchTicketViewModel.messageObserver().observe(this){message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecycler(data : MutableList<Schedule>,datas : MutableList<Schedule>){
        ticketAdapter = TicketAdapter(object : TicketAdapter.OnClick{
            override fun onClicked(schedule: Schedule,chairs : MutableList<Int>) {
                startActivity(Intent(this@SearchTicketActivity,DetailPerjalanan::class.java).also{
                    it.putExtra("schedule",schedule)
                       if(datas.size > 0){
                           it.putExtra("returnFlight",datas[0])
                       }
                    it.putIntegerArrayListExtra("chairs", ArrayList(chairs))
                })
            }

        })
        ticketAdapter.submitData(data)
        binding.recyclerSearch.apply {
            adapter = ticketAdapter
            layoutManager = LinearLayoutManager(this@SearchTicketActivity)
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