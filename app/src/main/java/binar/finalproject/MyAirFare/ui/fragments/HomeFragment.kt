package binar.finalproject.MyAirFare.ui.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.adapter.TicketAdapter
import binar.finalproject.MyAirFare.databinding.FragmentHomeBinding
import binar.finalproject.MyAirFare.model.tickets.Schedule
import binar.finalproject.MyAirFare.ui.activities.SearchTicketActivity
import binar.finalproject.MyAirFare.utils.DatePicker
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.ticket.TicketViewModel
import com.google.android.material.textfield.TextInputEditText
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), AdapterView.OnItemClickListener {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var ticketAdapter: TicketAdapter
    private lateinit var ticketViewModel: TicketViewModel
    private var listFrom : ArrayList<String> = arrayListOf()
    private var listDest : ArrayList<String> = arrayListOf()
    private lateinit var searchableSpinnerFrom : SearchableSpinner
    private lateinit var searchableSpinnerDest : SearchableSpinner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(requireActivity())[AuthPreferencesViewModel::class.java]
        ticketViewModel = ViewModelProvider(requireActivity())[TicketViewModel::class.java]
        searchableSpinnerFrom = SearchableSpinner(requireActivity())
        searchableSpinnerDest = SearchableSpinner(requireActivity())
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setDate()
        search()
        getAllTicket()
        isReturn()
        selectSpinner()
        val classes = resources.getStringArray(R.array.classes)
        val type = resources.getStringArray(R.array.type)
        dropDownMenu(classes,R.layout.dropdown_tittle_item,binding.tvClass)
        dropDownMenu(type,R.layout.dropdown_tittle_item,binding.tvTypeTicket)
        binding.apply {
            etDateReturn.focusable = View.NOT_FOCUSABLE
            etDateReturn.isFocusableInTouchMode = false
        }
    }
    private fun setupView(){
        authPreferencesViewModel.getName().observe(requireActivity()){
               if(it != null && it != ""){
                val name = "Hallo $it"
                binding.tvHallo.text = name
            }
        }
    }

    private fun dropDownMenu(arr : Array<String>, @LayoutRes layout : Int, tv : AutoCompleteTextView){
        val adapter = ArrayAdapter(requireActivity(), layout,arr)
        with(tv){
            setAdapter(adapter)
            onItemClickListener = this@HomeFragment
        }
    }

    private fun selectSpinner() {
        binding.apply {
            etFrom.setOnClickListener {
                spinnerDialog(searchableSpinnerFrom,etFrom,ArrayList(listFrom.sorted()))
            }

            etDestination.setOnClickListener {
                spinnerDialog(searchableSpinnerDest,etDestination,ArrayList(listDest.sorted()))
            }
        }
    }

    private fun spinnerDialog(searchableSpinner : SearchableSpinner,et : TextInputEditText,arr : ArrayList<String>){
        searchableSpinner.windowTitle = "Cari Asal Penerbangan"
        searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
            override fun setOnItemSelectListener(position: Int, selectedString: String) {
                et.setText(selectedString)
            }
        }
        searchableSpinner.windowTopBackgroundColor = R.color.primary
        searchableSpinner.setSpinnerListItems(arr)
        searchableSpinner.show()
    }

    override fun onItemClick(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if (parent == binding.tvClass) {
            binding.tvClass.setText(parent.getItemAtPosition(position).toString())
        } else if (parent == binding.tvTypeTicket) {
            binding.tvTypeTicket.setText(parent.getItemAtPosition(position).toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isReturn(){
        binding.switches.setOnCheckedChangeListener { _, checked ->
            if(checked){
                binding.apply {
                    setEnabled(true)
                    setAnimation(linearClassType,1.0f,View.VISIBLE)
                    setAnimation(ilLinearDateReturn,1.0f,View.VISIBLE)
                    val height = 650 * resources.displayMetrics.density
                    collapseConstraint.layoutParams.height =   height.toInt() //1280
                    collapseLinear.layoutParams.height = height.toInt()
                    btnSearch.setOnClickListener {
                       if(etDateReturn.text.toString().isNotEmpty()){
                           if(etDate.text.toString() != etDateReturn.text.toString()){
                               go(DatePicker.formatterDate(etDateReturn.text.toString()))
                           }else{
                               Toast.makeText(requireActivity(), "Tidak ada penerbangan pulang pergi di tanggal yang sama", Toast.LENGTH_SHORT).show()
                           }
                       }
                    }
                }
            }else{
                binding.apply {
                    setAnimation(linearClassType,0.0f,View.GONE)
                    setAnimation(ilLinearDateReturn,0.0f,View.GONE)
                    val height = 520 * resources.displayMetrics.density
                    collapseConstraint.layoutParams.height = height.toInt()
                    collapseLinear.layoutParams.height = height.toInt()
                    setEnabled(false)
                    search()
                }
            }

            binding.switches.isChecked = checked

        }
    }
    private fun setDate(){
        binding.etDate.setOnClickListener {
            DatePicker.datePicker(requireActivity(),binding.etDate)
        }
        binding.etDateReturn.setOnClickListener {
            DatePicker.datePicker(requireActivity(),binding.etDateReturn)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun search(){
        binding.apply {
            btnSearch.setOnClickListener {
                go(null)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun go(dateReturn : String?){
        binding.apply {
            val from = etFrom.text.toString()
            val destination = etDestination.text.toString()
            val date = etDate.text.toString()
            val kelas = tvClass.text.toString()
            val tipe = tvTypeTicket.text.toString()
            if(from.isNotBlank() && destination.isNotBlank() && date.isNotBlank() ){
                val dates = DatePicker.formatterDate(date)
                startActivity(Intent(requireActivity(),SearchTicketActivity::class.java).also{
                    it.putExtra("from",from)
                    it.putExtra("destination",destination)
                    it.putExtra("date",dates)
                    it.putExtra("kelas",kelas)
                    it.putExtra("tipe",tipe)
                    it.putExtra("dateReturn",dateReturn)
                })
            }else{
                Toast.makeText(requireActivity(), "Kolom asal dan tujuan wajib diisi", Toast.LENGTH_SHORT).show()
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
                       list.forEach { from ->
                           listFrom.add(from.from)
                       }
                       list.forEach { dest ->
                           listDest.add(dest.dest)
                       }
                   }
                }
            }
        }
    }


    private fun setRecycler(data : MutableList<Schedule>){
        ticketAdapter = TicketAdapter(object : TicketAdapter.OnClick{
            override fun onClicked(schedule: Schedule,chairs : MutableList<Int>) {
                // Not clicked in list ticket home
            }

        })
        ticketAdapter.submitData(data)
        binding.ticketRecycler.apply {
            adapter = ticketAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setEnabled(enable : Boolean){
        binding.apply {
            tvTypeTicket.text?.clear()
            tvClass.text?.clear()
            etDateReturn.text?.clear()
            etDateReturn.focusable = View.NOT_FOCUSABLE
            etDateReturn.isFocusableInTouchMode = false
            tvClass.focusable = View.NOT_FOCUSABLE
            tvClass.isFocusableInTouchMode = false
            tvTypeTicket.focusable = View.NOT_FOCUSABLE
            tvTypeTicket.isFocusableInTouchMode = false
            etDateReturn.isEnabled = enable
            tvTypeTicket.isEnabled = enable
            tvClass.isEnabled = enable
            if(enable){
                etDateReturn.visibility = View.VISIBLE
                tvTypeTicket.visibility = View.VISIBLE
                tvClass.visibility = View.VISIBLE
            }else{
                etDateReturn.visibility = View.GONE
                tvTypeTicket.visibility = View.GONE
                tvClass.visibility = View.GONE
            }
        }


    }

    private fun setAnimation(anim : LinearLayout,alpha : Float, i : Int){
        anim.animate()
            .alpha(alpha)
            .setDuration(100)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    anim.visibility = i
                }
            })
    }

    private fun showLoading(show : Boolean){
        if(show) binding.loading.visibility = View.VISIBLE else  binding.loading.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }




}