@file:Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused"
)

package binar.finalproject.MyAirFare.ui.activities

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.databinding.ActivityPaymentDetailsBinding
import binar.finalproject.MyAirFare.model.tickets.Schedule
import binar.finalproject.MyAirFare.utils.DatePicker
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.CurrentUserViewModel
import com.bumptech.glide.Glide
import com.midtrans.sdk.corekit.core.MidtransSDK
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentDetailsActivity : AppCompatActivity() {
    private var _binding : ActivityPaymentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUserViewModel: CurrentUserViewModel
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPaymentDetailsBinding.inflate(layoutInflater)
        currentUserViewModel = ViewModelProvider(this)[CurrentUserViewModel::class.java]
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        setContentView(binding.root)
        back()
        startPayment()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun startPayment(){
        binding.btnCheckout.setOnClickListener {
            MidtransSDK.getInstance().startPaymentUiFlow(applicationContext,"6fdeefde-f5ff-40cb-a7eb-58cf63d6d88b")
        }


//        val transactionRequest = TransactionRequest("MyAirFare",100.000)
//        authPreferencesViewModel.getToken().observe(this){ token ->
//            currentUserViewModel.currentUser(token)
//            currentUserViewModel.currentUserObserver().observe(this){
//                if(it != null){
////                    customerDetails(
////                        transactionRequest,
////                        it.user.username,
////                        it.user.f_name,it.user.l_name,
////                        it.user.email,binding.etNomor.text.toString())
//                    setupView(it.user.email,it.user.f_name,it.user.l_name)
//                    binding.btnCheckout.setOnClickListener {
////                        MidtransSDK.getInstance().transactionRequest = transactionRequest
//                        MidtransSDK.getInstance().startPaymentUiFlow(applicationContext,"")
//                    }
//                }else{
//                    currentUserViewModel.messageObserver().observe(this){ message ->
//                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
    }

//    private fun customerDetails(
//        transactionRequest: TransactionRequest,
//        username : String,firstname : String,lastname : String,email :String,phone : String
//    ){
//        val customerDetails = CustomerDetails()
//        customerDetails.customerIdentifier = username
//        customerDetails.firstName = firstname
//        customerDetails.lastName = lastname
//        customerDetails.email = email
//        customerDetails.phone = phone
//        transactionRequest.customerDetails = customerDetails
//    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupView(email : String, f_name : String, lastname : String){
        val diff = intent.getParcelableExtra<Schedule>("schedule")
        if(diff != null){
            binding.apply {
                val dateAir = DatePicker.dateCalculation(diff.date_air)
                val estimated = DatePicker.dateCalculation(diff.estimated_up_dest)
                val timeAir = DatePicker.timeCalculation(diff.date_air)
                val timeEstimated = DatePicker.timeCalculation(diff.estimated_up_dest)
                val date = "$dateAir - $estimated"
                Glide.with(root).load("https://binarstudpenfinalprojectbe-production.up.railway.app${diff.logo}").into(imageLogo)
                tvTime.text = DatePicker.getDifferentTime(diff.date_air)
                tvDate.text = date
                tvAsalTime.text = timeAir
                tvTujuanTime.text = timeEstimated
                when(diff.kelas){
                    1 ->  tvClass.text = "ECONOMY"
                    2 ->  tvClass.text = "BUSSINESS"
                }
                tvAsal.text = diff.from
                tvTujuan.text = diff.dest
                tvPrice.text = diff.price.toString()
                tvKodePenerbangan.text = diff.flight_number
                etEmail.setText(email)
                val fullName = "$f_name $lastname"
                etNamaLengkap.setText(fullName)
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