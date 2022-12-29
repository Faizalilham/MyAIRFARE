@file:Suppress("LocalVariableName")

package binar.finalproject.MyAirFare.ui.activities


import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.ActivityEditProfileBinding
import binar.finalproject.MyAirFare.databinding.ImageBottomSheetBinding
import binar.finalproject.MyAirFare.model.user.CurrentUser
import binar.finalproject.MyAirFare.utils.EditProfileValidation
import binar.finalproject.MyAirFare.utils.ImagePost
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.CurrentUserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private var _binding : ActivityEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUserViewModel: CurrentUserViewModel
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var uri : Uri
    private var getFile: File? = null
    private var tittle = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditProfileBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        currentUserViewModel = ViewModelProvider(this)[CurrentUserViewModel::class.java]
        setContentView(binding.root)
        back()
        getDetail()
        bottomSheet()
        updateProfile()
        dropDownMenu()

    }

    private fun dropDownMenu(){
        val data = resources.getStringArray(R.array.tittle_item)
        val adapter = ArrayAdapter(this, R.layout.dropdown_tittle_item,data)
        with(binding.tvTittle){
            setAdapter(adapter)
            onItemClickListener = this@EditProfileActivity
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        tittle = item
    }

    private fun getDetail(){
        val i = intent.getParcelableExtra<CurrentUser>("user")
        binding.apply {
            if(i != null){
                val a = i.user.username.split(".")
                if(a.size > 1){
                    etUsername.setText(a[1])
                    tvTittle.setText(a[0])
                }else{
                    etUsername.setText(a[0])
                }
                etLastname.setText(i.user.l_name)
                etFirstname.setText(i.user.f_name)
                etEmail.setText(i.user.email)
                etPassword.setText(i.user.password)
                etConfirmPassword.setText(i.user.password)
                etVisaNumber.setText(i.user.visa_ID)
                Glide.with(root).load("https://binarstudpenfinalprojectbe-production-77a5.up.railway.app${i.user.photo}").into(imageProfile)
                etConfirmPassword.isEnabled = false
                etPassword.isEnabled = false
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun bottomSheet(){
       binding.fabImagePick.setOnClickListener {
           val bottomSheet = BottomSheetDialog(this)
           val view = ImageBottomSheetBinding.inflate(layoutInflater)
           bottomSheet.apply {
              view.apply {
                  setContentView(root)
                  show()
                  cardCamera.setOnClickListener {
                      if(checkPermission()){ openCamera() }
                  }
                  cardGallery.setOnClickListener {
                      if(checkPermission()){ openGallery() }
                  }
              }
           }
       }
    }

    private fun upload() {
        if(getFile != null){
            val file = ImagePost.reduceFileImage(getFile as File)
            binding.apply {
                val username = etUsername.text.toString()
                val f_name = etFirstname.text.toString()
                val l_name = etLastname.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()
                val tittle = tvTittle.text.toString()
                val visaNumber = etVisaNumber.text.toString()

                val currentImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    currentImageFile
                )
                val editValidation = EditProfileValidation.editProfile(username,f_name,l_name,password,confirmPassword,email,tittle)

                if(editValidation == "success"){
                   authPreferencesViewModel.getToken().observe(this@EditProfileActivity){
                       if(it != null){
                           currentUserViewModel.currentUserUpdate(
                               it,
                               username.toRequestBody("text/plain".toMediaTypeOrNull()),
                               f_name.toRequestBody("text/plain".toMediaTypeOrNull()),
                               l_name.toRequestBody("text/plain".toMediaTypeOrNull()),
                               email.toRequestBody("text/plain".toMediaTypeOrNull()),
                               password.toRequestBody("text/plain".toMediaTypeOrNull()),
                               confirmPassword.toRequestBody("text/plain".toMediaTypeOrNull()),
                               tittle.toRequestBody("text/plain".toMediaTypeOrNull()),
                               imageMultipart,
                               visaNumber.toRequestBody("text/plain".toMediaTypeOrNull())
                           )
                       }else{
                           currentUserViewModel.messageObserver().observe(this@EditProfileActivity){ message ->
                               Toast.makeText(this@EditProfileActivity, message, Toast.LENGTH_SHORT).show()
                           }
                       }
                   }
                    currentUserViewModel.currentUserUpdateObserver().observe(this@EditProfileActivity){
                        if(it != null && it.update.size > 0){
                            if(it.update[0] == 1){
                                finish()
                                Toast.makeText(this@EditProfileActivity, "Edit Profile berhasil", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(this@EditProfileActivity, "Edit Profile gagal", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            currentUserViewModel.messageObserver().observe(this@EditProfileActivity){ message ->
                                Toast.makeText(this@EditProfileActivity, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }else{
                    Toast.makeText(this@EditProfileActivity, editValidation, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun updateProfile(){
        binding.btnUpdate.setOnClickListener {
            upload()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA,
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
            Toast.makeText(this@EditProfileActivity, "Permission  DITOLAK", Toast.LENGTH_LONG).show()
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


    private fun back(){
        binding.toolbar.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}