@file:Suppress("RedundantSamConstructor")

package binar.finalproject.MyAirFare.utils

import android.content.Context
import android.widget.Toast
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.snap.Gopay
import com.midtrans.sdk.corekit.models.snap.Shopeepay
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder

object MidtransConfig {

    fun setupMidTrans(context : Context){
        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-cCzg38meP7fK1aYH")
            .setContext(context)
            .setTransactionFinishedCallback(TransactionFinishedCallback { result ->
                if (result.response != null) {
                    when (result.status) {
                        TransactionResult.STATUS_SUCCESS -> Toast.makeText(
                            context,
                            "Transaction Finished. ID: " + result.response.transactionId,
                            Toast.LENGTH_LONG
                        ).show()
                        TransactionResult.STATUS_PENDING -> Toast.makeText(
                            context,
                            "Transaction Pending. ID: " + result.response.transactionId,
                            Toast.LENGTH_LONG
                        ).show()
//                        TransactionResult.STATUS_FAILED -> Toast.makeText(
//                            context,
//                            "Transaction Failed. ID: " + result.response.transactionId
//                                .toString() + ". Message: " + result.response
//                                .statusMessage,
//                            Toast.LENGTH_LONG
//                        ).show()
                    }
                    result.response.validationMessages
                } else if (result.isTransactionCanceled) {
//                    Toast.makeText(context, "Transaction Canceled", Toast.LENGTH_LONG).show()
                } else {
                    if (result.status.equals(TransactionResult.STATUS_INVALID)) {
                        Toast.makeText(context, "Transaction Invalid", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Transaction Finished with failure.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
            .setMerchantBaseUrl("https://binarstudpenfinalprojectbe-production.up.railway.app/") // Url server
            .enableLog(true)
            .setUIkitCustomSetting(uiKitCustomSetting())
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .setLanguage("id")
            .buildSDK()
    }

    fun initTransactionRequest(): TransactionRequest {
        // Create new Transaction Request
        val transactionRequestNew = TransactionRequest(System.currentTimeMillis().toString() + "", 36500.0)
        transactionRequestNew.customerDetails = initCustomerDetails()
        transactionRequestNew.gopay = Gopay("mysamplesdk:://midtrans")
        transactionRequestNew.shopeepay = Shopeepay("mysamplesdk:://midtrans")
        return transactionRequestNew
    }

    private fun initCustomerDetails(): CustomerDetails {
        //define customer detail (mandatory for coreflow)
        val mCustomerDetails = CustomerDetails()
        mCustomerDetails.phone = "085310102020"
        mCustomerDetails.firstName = "user fullname"
        mCustomerDetails.email = "mail@mail.com"
        mCustomerDetails.customerIdentifier = "mail@mail.com"
        return mCustomerDetails
    }

    private fun uiKitCustomSetting(): UIKitCustomSetting {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.isSkipCustomerDetailsPages = true
        uIKitCustomSetting.isShowPaymentStatus = true
        return uIKitCustomSetting
    }
}
