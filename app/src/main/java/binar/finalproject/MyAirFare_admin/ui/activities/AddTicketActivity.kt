package binar.finalproject.MyAirFare_admin.ui.activities

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.utils.ImagePost
import binar.finalproject.MyAirFare_admin.viewmodel.auth.AuthPreferencesViewModel
import binar.finalproject.MyAirFare_admin.R
import binar.finalproject.MyAirFare_admin.databinding.ActivityAddTicketBinding
import binar.finalproject.MyAirFare_admin.databinding.ImageBottomSheetBinding
import binar.finalproject.MyAirFare_admin.utils.DatePicker
import binar.finalproject.MyAirFare_admin.utils.PostTicketValidation
import binar.finalproject.MyAirFare_admin.utils.TicketConstant
import binar.finalproject.MyAirFare_admin.viewmodel.ticket.TicketViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class AddTicketActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private var _binding : ActivityAddTicketBinding? = null
    private val binding get() = _binding!!
    private lateinit var ticketViewModel: TicketViewModel
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var uri : Uri
    private var getFile: File? = null
    private var type = ""
    private var id : String? = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddTicketBinding.inflate(layoutInflater)
        ticketViewModel = ViewModelProvider(this)[TicketViewModel::class.java]
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        setContentView(binding.root)
        binding.etDateFlight.setText(DatePicker.getCurrentDate())
        id = intent.getStringExtra("id")
        if(id != null){
            setupView(id!!)
            binding.tvTittle.text = "Perbarui Tiket"
            binding.btnPost.visibility = View.GONE
        }else{
            binding.tvTittle.text = "Tambah Tiket"
            binding.btnUpdate.visibility = View.GONE
        }
        dropDownMenu()
        bottomSheet()
        postTicket()
        updateTicket()
        back()
    }

    private fun back(){
        binding.toolbar.setOnClickListener { finish() }
    }

    private fun dropDownMenu(){
        val data = resources.getStringArray(R.array.type)
        val adapter = ArrayAdapter(this,R.layout.dropdown_type_ticket,data)
        with(binding.tvTypeTicket){
            setAdapter(adapter)
            onItemClickListener = this@AddTicketActivity
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        type = item
    }

    private val openGallery = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null){
            val myFile = ImagePost.uriToFile(it, this)
            getFile = myFile
            binding.imageProfile.setImageURI(it)
        }
    }
    private fun openGallery(){
        intent.type = "image/*"
        openGallery.launch("image/*")
    }

    private val openCamera = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if(it){
            binding.imageProfile.setImageURI(uri)
        }
    }

    private fun openCamera(){
        uri = FileProvider.getUriForFile(this,"${this.packageName}.provider", ImagePost.createFile(this))
        getFile = ImagePost.uriToFile(uri,this)
        openCamera.launch(uri)
    }

    private fun bottomSheet(){
        binding.fabImagePick.setOnClickListener {
            val bottomSheet = BottomSheetDialog(this)
            val view = ImageBottomSheetBinding.inflate(layoutInflater)
            bottomSheet.apply {
                view.apply {
                    setContentView(root)
                    show()
                    cardCamera.setOnClickListener {
                        openCamera()
                    }
                    cardGallery.setOnClickListener {
                        openGallery()
                    }
                }
            }
        }
    }


    private fun upload(){
        if(getFile != null){
            val file = ImagePost.reduceFileImage(getFile as File)
            val types = intent.getIntExtra("types",0)
            binding.apply{
                val airlane = etNamePlane.text.toString()
                val from = etFrom.text.toString()
                val dest = etDestination.text.toString()
                val dest_air = etDateFlight.text.toString()
                val price = etPrice.text.toString()
                val chair = etNoChair.text.toString()
                val flightNumber = etFlightNumber.text.toString()
                val kelas = etclass.text.toString()
                val estimated = etEstimated.text.toString()
                val currentImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    currentImageFile
                )
                Log.d("ALL DATA","$airlane,$from,$dest,$dest_air,$price,$chair,$type,$imageMultipart,$flightNumber,$flightNumber,$kelas,$estimated")
                val validation = PostTicketValidation.postTicketValidation(airlane,from,dest,dest_air,price.toInt(),chair.toInt(),type,flightNumber,estimated,kelas)
                if(validation == "success"){
                    authPreferencesViewModel.getToken().observe(this@AddTicketActivity){
                        Log.d("TOUKEN","$it $id")
                        if(it != null && it != "undefined"){
                            if(id !=  null && types == TicketConstant.UPDATE){
                                ticketViewModel.doUpdateTicket(
                                    it, id!!,
                                    airlane.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    from.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    dest.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    dest_air.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    price.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    chair.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    type.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    imageMultipart,
                                    flightNumber.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    kelas.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    estimated.toRequestBody("text/plain".toMediaTypeOrNull())
                                )
                                setToast(ticketViewModel.doUpdateTicketObserver(),ticketViewModel.messageObserver(),"Update Tiket Berhasil")
                            }else{
                                ticketViewModel.addTicket(
                                    it,
                                    airlane.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    from.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    dest.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    dest_air.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    price.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    chair.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    type.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    imageMultipart,
                                    flightNumber.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    kelas.toRequestBody("text/plain".toMediaTypeOrNull()),
                                    estimated.toRequestBody("text/plain".toMediaTypeOrNull())
                                )
                                setToast(ticketViewModel.addTicketObserver(),ticketViewModel.messageObserver(),"Tambah Tiket Berhasil")
                            }
                        }
                    }
                    
                    
                }else{
                    Toast.makeText(this@AddTicketActivity, validation, Toast.LENGTH_SHORT).show()
                }

            }
        }else{
            Toast.makeText(this, "Tolong Masukkan Gambar", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun <T> setToast(a : LiveData<T>, b :LiveData<String>,message : String){
        a.observe(this@AddTicketActivity){
            if(it != null){
                binding.btnPost.visibility = View.VISIBLE
                binding.btnUpdate.visibility = View.VISIBLE
                Toast.makeText(this@AddTicketActivity, message , Toast.LENGTH_SHORT).show()
                finish()
            }else{
                b.observe(this@AddTicketActivity){ message ->
                    Toast.makeText(this@AddTicketActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupView(id : String){
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
                ticketViewModel.readTicketById(it,id)
                ticketViewModel.readTicketByIdObserver().observe(this){ datas ->
                    if(datas != null){
                        binding.apply {
                            etNamePlane.setText(datas.ticket.name)
                            etFrom.setText(datas.ticket.from)
                            etDestination.setText(datas.ticket.dest)
                            etclass.setText(datas.ticket.kelas)
                            etNoChair.setText(datas.ticket.no_chair.toString())
                            etPrice.setText((datas.ticket.price.toString()))
                            etEstimated.setText(datas.ticket.estimated_up_dest)
                            etDateFlight.setText(datas.ticket.date_air)
                            etFlightNumber.setText(datas.ticket.flight_number)
                            Glide.with(root).load("https://binarstudpenfinalprojectbe-production.up.railway.app${datas.ticket.logo}").into(imageProfile)
                        }
                    }
                }
            }
        }
    }

    private fun postTicket(){
        binding.btnPost.setOnClickListener {
            upload()
        }
    }

    private fun updateTicket(){
        binding.btnUpdate.setOnClickListener {
            upload()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}