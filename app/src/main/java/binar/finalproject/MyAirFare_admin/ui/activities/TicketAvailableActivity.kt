package binar.finalproject.MyAirFare_admin.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.MyAirFare_admin.R
import binar.finalproject.MyAirFare_admin.adapter.TicketAdapter
import binar.finalproject.MyAirFare_admin.databinding.ActivityTicketAvailableBinding
import binar.finalproject.MyAirFare_admin.model.ticket.Tickets
import binar.finalproject.MyAirFare_admin.utils.TicketConstant
import binar.finalproject.MyAirFare_admin.viewmodel.auth.AuthPreferencesViewModel
import binar.finalproject.MyAirFare_admin.viewmodel.ticket.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketAvailableActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
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
        doSearch()
        dropDownMenuClass()
        dropDownMenuType()
        back()
    }

    private fun dropDownMenuType(){
        val arrayType = resources.getStringArray(R.array.type)
        val adapter = ArrayAdapter(this, R.layout.dropdown_type_ticket,arrayType)
        with(binding.tvTypeTicket){
            setAdapter(adapter)
            onItemClickListener = this@TicketAvailableActivity
        }
    }
    private fun dropDownMenuClass(){
        val arrayClass = resources.getStringArray(R.array.classes)
        val adapter = ArrayAdapter(this, R.layout.dropdown_type_ticket,arrayClass)
        with(binding.tvClass){
            setAdapter(adapter)
            onItemClickListener = this@TicketAvailableActivity
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent == binding.tvClass) {
            binding.tvClass.setText(parent.getItemAtPosition(position).toString())
        } else if (parent == binding.tvTypeTicket) {
            binding.tvTypeTicket.setText(parent.getItemAtPosition(position).toString())
        }

    }

    private fun searchTicket(from : String,dest : String,depart : String, kelas : String, type : String){
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
                ticketViewModel.doFilterTicket(
                    from, dest, depart, kelas, type
                )
                showLoading(true)
                ticketViewModel.doFilterTicketObserver().observe(this){ filterSearch ->
                    if(filterSearch != null){
                        if(filterSearch.tickets.go.size > 0){
                            showLoading(false)
                            showFound(true)
                            setupRecycler(filterSearch.tickets.go)
                            Toast.makeText(this@TicketAvailableActivity, "${filterSearch.tickets.go}", Toast.LENGTH_SHORT).show()
                        }else{
                            showLoading(false)
                            showFound(false)
                        }
                    }else{
                        ticketViewModel.messageObserver().observe(this){ message ->
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun doSearch(){
        binding.apply {
            btnSearch.setOnClickListener {
                val from = etFrom.text.toString().trim()
                val dest = etDestination.text.toString().trim()
                val depart = etDate.text.toString().trim()
                val kelas = tvClass.text.toString().trim()
                val type = tvTypeTicket.text.toString().trim()
                if(from.isNotBlank() && dest.isNotBlank() && depart.isNotBlank() && kelas.isNotBlank()){
                    searchTicket(from,dest,depart,kelas,type)
                }else{
                    Toast.makeText(this@TicketAvailableActivity, "Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
        ticketAdapter.submitData(mutableListOf())
        ticketAdapter.submitData(datas)
        binding.recyclerTicket.apply {
            adapter = ticketAdapter
            layoutManager = LinearLayoutManager(this@TicketAvailableActivity)
        }
    }

    private fun showLoading(show : Boolean){
        if(show){
            binding.loading.visibility = View.VISIBLE
        } else  {
            binding.loading.visibility = View.GONE
        }
    }

    private fun showFound(found : Boolean){
       binding.apply {
           if(found) {
               imageNotFound.visibility =  View.GONE
               tvNotFound.visibility =  View.GONE
               binding.recyclerTicket.visibility = View.VISIBLE
           }else {
               imageNotFound.visibility =  View.VISIBLE
               tvNotFound.visibility  = View.VISIBLE
               binding.recyclerTicket.visibility = View.GONE

           }
       }
    }

    private fun back(){
        binding.toolbar.setOnClickListener { finish() }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}