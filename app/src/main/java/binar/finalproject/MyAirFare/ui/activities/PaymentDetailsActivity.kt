package binar.finalproject.MyAirFare.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.databinding.ActivityPaymentDetailsBinding
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.CurrentUserViewModel
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentDetailsActivity : AppCompatActivity() {
    private var _binding : ActivityPaymentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUserViewModel: CurrentUserViewModel
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPaymentDetailsBinding.inflate(layoutInflater)
        currentUserViewModel = ViewModelProvider(this)[CurrentUserViewModel::class.java]
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        setContentView(binding.root)
        back()
        setupMidTrans()
        startPayment()
    }

    private fun setupMidTrans(){
        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-cCzg38meP7fK1aYH")
            .setContext(applicationContext)
            .setTransactionFinishedCallback(TransactionFinishedCallback { result ->
                if (result.response != null) {
                    when (result.status) {
                        TransactionResult.STATUS_SUCCESS -> Toast.makeText(
                            this,
                            "Transaction Finished. ID: " + result.response.transactionId,
                            Toast.LENGTH_LONG
                        ).show()
                        TransactionResult.STATUS_PENDING -> Toast.makeText(
                            this,
                            "Transaction Pending. ID: " + result.response.transactionId,
                            Toast.LENGTH_LONG
                        ).show()
                        TransactionResult.STATUS_FAILED -> Toast.makeText(
                            this,
                            "Transaction Failed. ID: " + result.response.transactionId
                                .toString() + ". Message: " + result.response
                                .statusMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    result.response.validationMessages
                } else if (result.isTransactionCanceled) {
                    Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show()
                } else {
                    if (result.status.equals(TransactionResult.STATUS_INVALID)) {
                        Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Transaction Finished with failure.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
            .setMerchantBaseUrl("")
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .setLanguage("id")
            .buildSDK()
    }
    private fun startPayment(){
        val transactionRequest = TransactionRequest("MyAirFare",100.000)
        authPreferencesViewModel.getToken().observe(this){ token ->
            currentUserViewModel.currentUser(token)
            currentUserViewModel.currentUserObserver().observe(this){
                if(it != null){
                    customerDetails(
                        transactionRequest,
                        it.user.username,
                        it.user.f_name,it.user.l_name,
                        it.user.email,binding.etNomor.text.toString())
                    setupView(it.user.email,it.user.f_name,it.user.l_name)
                    binding.btnCheckout.setOnClickListener {
                        MidtransSDK.getInstance().transactionRequest = transactionRequest
                        MidtransSDK.getInstance().startPaymentUiFlow(applicationContext,"")
                    }
                }else{
                    currentUserViewModel.messageObserver().observe(this){ message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupView(email : String,f_name : String,lastname : String){
        val i = intent.getStringExtra("kode")
        if(i != null){
            binding.apply {
                kodePenerbangan.text = i
                etEmail.setText(email)
                val fullName = "$f_name $lastname"
                etNamaLengkap.setText(fullName)
            }
        }

    }

    private fun customerDetails(
        transactionRequest: TransactionRequest,
        username : String,firstname : String,lastname : String,email :String,phone : String
    ){
        val customerDetails = CustomerDetails()
        customerDetails.customerIdentifier = username
        customerDetails.firstName = firstname
        customerDetails.lastName = lastname
        customerDetails.email = email
        customerDetails.phone = phone
        transactionRequest.customerDetails = customerDetails
    }


    private fun back(){
        binding.toolbar.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}