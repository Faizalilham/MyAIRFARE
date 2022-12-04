package binar.finalproject.MyAirFare.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.MyAirFare.adapter.TicketAdapter
import binar.finalproject.MyAirFare.databinding.ActivitySearchTicketBinding
import binar.finalproject.MyAirFare.model.flight.Schedule
import binar.finalproject.MyAirFare.utils.DatePicker
import binar.finalproject.MyAirFare.viewmodel.ticket.SearchTicketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchTicketActivity : AppCompatActivity() {
    private var _binding : ActivitySearchTicketBinding? = null
    private val binding get() = _binding!!
    private lateinit var ticketAdapter: TicketAdapter
    private lateinit var searchTicketViewModel: SearchTicketViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchTicketBinding.inflate(layoutInflater)
        searchTicketViewModel = ViewModelProvider(this)[SearchTicketViewModel::class.java]
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
        val time =  intent.getStringExtra("time")
        val dateTime = "$date $time"
        if(from != null && dest != null){
            doSearch(from,dest,dateTime)
            Log.d("TANGGAL su",dateTime)
            Toast.makeText(this, dateTime, Toast.LENGTH_SHORT).show()
            binding.apply {
                etKeberangkatan.setText(from)
                etTiba.setText(dest)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doSearch(from : String, dest : String,dateTime :String){
        Log.d("TANGGAL","12/06/2022 10:00:00")
        searchTicketViewModel.doSearchTicket(from,dest,dateTime)
        searchTicketViewModel.doSearchTicketObserver().observe(this){
            if(it != null){
                if(it.tickets.go.size > 1){
                    binding.apply {
                        imageNotFound.visibility = View.GONE
                        tvNotFound.visibility = View.GONE
                    }
                    setupRecycler(it.tickets.go)
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

    private fun setupRecycler(data : MutableList<Schedule>){
        ticketAdapter = TicketAdapter(object : TicketAdapter.OnClick{
            override fun onClicked(schedule: Schedule) {
                startActivity(Intent(this@SearchTicketActivity,DetailPerjalanan::class.java).also{
                    it.putExtra("schedule",schedule)
                })
            }

        })
        ticketAdapter.submitData(data)
        binding.recyclerSearch.apply {
            adapter = ticketAdapter
            layoutManager = LinearLayoutManager(this@SearchTicketActivity)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}