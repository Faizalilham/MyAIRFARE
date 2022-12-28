package binar.finalproject.MyAirFare.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.databinding.ActivityScanQractivityBinding
import binar.finalproject.MyAirFare.model.room.CheckIn
import binar.finalproject.MyAirFare.utils.ScannerConfig
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.checkin.CheckInRoomViewModel
import binar.finalproject.MyAirFare.viewmodel.checkin.CheckinViewModel
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ScanQRActivity : AppCompatActivity() {
    private var _binding : ActivityScanQractivityBinding? = null
    private val binding get() = _binding!!
    private lateinit var codeScanner : CodeScanner
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var checkinViewModel: CheckinViewModel
    private lateinit var checkInRoomViewModel : CheckInRoomViewModel
    var data : String = ""
    private val chairs = mutableListOf<Int>()
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityScanQractivityBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        checkinViewModel = ViewModelProvider(this)[CheckinViewModel::class.java]
        checkInRoomViewModel = ViewModelProvider(this)[CheckInRoomViewModel::class.java]
        setContentView(binding.root)
        resultScanner()
        if(!checkPermission()){
            finish()
        }
    }

    private fun doCheckIn(data : String){
        if(data.isNotEmpty()){
            val datas = data.uppercase(Locale.getDefault())
            checkIn(datas)
        }
    }

    private fun resultScanner(){
        codeScanner = CodeScanner(this,binding.scannerView)
        ScannerConfig.doScanQR(codeScanner)
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                data = it.text
                doCheckIn(data.trim())
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
        binding.scannerView.setOnClickListener { codeScanner.startPreview() }
    }

    private fun checkIn(flightNumber : String){
        authPreferencesViewModel.getToken().observe(this){
            if(it != null && it != "undefined"){
                Toast.makeText(this,"$it $flightNumber", Toast.LENGTH_SHORT).show()
                checkinViewModel.doCheckInAuto(it,flightNumber)
                showLoading(true)
                checkinViewModel.doCheckInAutoObserver().observe(this){result ->
                    showLoading(false)
                    if(result != null){
                        finish()
                        Log.d("HASIL",result.toString())
                        result.trxs.forEach { trxs ->
                            trxs.carts.forEach {  carts ->
                                if(trxs.carts.size > 1){
                                    if(carts.chair_number != null){
                                        chairs.add(carts.chair_number)
                                        val chairsResult = chairs.sorted().joinToString(",")
                                        addCheckInToRoom(trxs.order_id,carts.ticket.from,carts.ticket.dest,chairsResult,"Berhasil CheckIn",carts.ticket.date_air)

                                    }
                                }else{
                                    addCheckInToRoom(trxs.order_id,carts.ticket.from,carts.ticket.dest,carts.chair_number.toString(),"Berhasil CheckIn",carts.ticket.date_air)
                                }
                            }
                        }
                        Toast.makeText(this, "CheckIn Berhasil", Toast.LENGTH_SHORT).show()
                    }else{
                        checkinViewModel.messageObserver().observe(this){message ->
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun addCheckInToRoom(order_id :String,from : String,
                                 dest : String,chair : String,status : String,date : String){
        checkInRoomViewModel.insertCheckIn(CheckIn(
            0,order_id,from,dest,chair,status,date
        ))
    }

    private fun showLoading(showLoading : Boolean){
        binding.apply {
            if(showLoading){
                loading.visibility = View.VISIBLE
                loadingBg.visibility = View.VISIBLE
            }else{
                loading.visibility = View.GONE
                loadingBg.visibility = View.GONE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestLocationPermission() {
        requestPermissions(arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE), 201)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission():Boolean{
        val permissionCheck = checkSelfPermission(Manifest.permission.CAMERA)
        val permissionCheck2 = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionCheck3 = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return if (permissionCheck == PackageManager.PERMISSION_GRANTED &&
            permissionCheck2 == PackageManager.PERMISSION_GRANTED &&
            permissionCheck3 == PackageManager.PERMISSION_GRANTED    ) {
            true
        } else {
            Toast.makeText(this, "Permission  DITOLAK", Toast.LENGTH_LONG).show()
            requestLocationPermission()
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            201 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    permissions[0] == Manifest.permission.CAMERA && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    permissions[1] == Manifest.permission.WRITE_EXTERNAL_STORAGE  &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    permissions[2] == Manifest.permission.READ_EXTERNAL_STORAGE
                ) {
                    Toast.makeText(this, "Permissions Permitted", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(this, "Permissions Denied", Toast.LENGTH_LONG)
                        .show()
                }
            }
            else -> {
                Toast.makeText(this, "The request code doesn't match", Toast.LENGTH_LONG).show()
            }
        }
    }



    override fun onResume() {
        codeScanner.startPreview()
        super.onResume()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}