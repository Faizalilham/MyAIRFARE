package binar.finalproject.MyAirFare_admin.ui.activities

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import binar.finalproject.MyAirFare_admin.databinding.ActivityQrcodeBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder


class QRCodeActivity : AppCompatActivity() {
    private var _binding : ActivityQrcodeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnGenerateQR.setOnClickListener {
            val text = binding.etFlightNumber.text.toString().trim()
            if(text.isNotEmpty() && text.isNotBlank()){
                generateQRCode(text)
            }
        }
        back()
    }

    private fun back(){
        binding.toolbar.setOnClickListener { finish() }
    }

    private fun generateQRCode(text : String){
        val mWriter = MultiFormatWriter()
        try {
            val mMatrix = mWriter.encode(text, BarcodeFormat.QR_CODE, 400, 400)
            val mEncoder = BarcodeEncoder()
            val mBitmap = mEncoder.createBitmap(mMatrix)
            binding.imageQRCode.setImageBitmap(mBitmap)
//            val manager: InputMethodManager =
//                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            manager.hideSoftInputFromWindow(binding.etFlightNumber.applicationWindowToken, 0)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}