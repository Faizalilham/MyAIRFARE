package binar.finalproject.MyAirFare.ui.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import binar.finalproject.MyAirFare.databinding.ActivityEditProfileBinding
import binar.finalproject.MyAirFare.databinding.ImageBottomSheetBinding
import binar.finalproject.MyAirFare.model.user.CurrentUser
import binar.finalproject.MyAirFare.utils.ImagePost
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File

class EditProfileActivity : AppCompatActivity() {
    private var _binding : ActivityEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var uri : Uri
    private var getFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        back()
        getDetail()
        bottomSheet()

    }

    private fun getDetail(){
        val i = intent.getParcelableExtra<CurrentUser>("user")
        binding.apply {
            if(i != null){
                etUsername.setText(i.user.username)
                etLastname.setText(i.user.l_name)
                etFirstname.setText(i.user.f_name)
                etEmail.setText(i.user.email)
                etPassword.setText(i.user.password)
                etConfirmPassword.setText(i.user.password)
                Glide.with(root).load(i.user.photo).into(imageProfile)
            }
        }
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
        uri = FileProvider.getUriForFile(this,"${this.packageName}.provider",ImagePost.createFile(this))
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

    private fun back(){
        binding.toolbar.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}