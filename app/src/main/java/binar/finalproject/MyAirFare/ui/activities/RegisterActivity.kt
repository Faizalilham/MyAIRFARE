package binar.finalproject.MyAirFare.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.ActivityRegisterBinding
import binar.finalproject.MyAirFare.utils.AuthValidation
import binar.finalproject.MyAirFare.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private var _binding : ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel : AuthViewModel
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var auth : FirebaseAuth
    private var tittle = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        setContentView(binding.root)
        doLogin()
        dropDownMenu()
        doRegister()
        doSignInWithGoogle()
    }

    private fun doLogin(){
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }


    private fun dropDownMenu(){
        val data = resources.getStringArray(R.array.tittle_item)
        val adapter = ArrayAdapter(this,R.layout.dropdown_tittle_item,data)
        with(binding.tvTittle){
            setAdapter(adapter)
            onItemClickListener = this@RegisterActivity
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        tittle = item
    }

    private fun doRegister(){
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val firstname = binding.etFirstname.text.toString()
            val lastname = binding.etLastname.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            val validation = AuthValidation.registerValidation(username,firstname,lastname,email,password,confirmPassword, tittle)
            if(validation == "success"){
               authViewModel.doRegister(username,firstname,lastname,email,password,confirmPassword,tittle)
               authViewModel.doRegisterObserver().observe(this){
                   if(it != null){
                       Toast.makeText(this, "Berhasil Daftar", Toast.LENGTH_SHORT).show()
                       startActivity(Intent(this,VerificationActivity::class.java).also{its->
                           its.putExtra("email",email)
                       })
                   }else{
                       authViewModel.messageObserver().observe(this){ message ->
                           if(message != null){
                               Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                           }
                       }
                   }
               }
            }else{
                Toast.makeText(this, validation, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun doSignInWithGoogle(){
        binding.btnGoogle.setOnClickListener {
            signInGoogle()
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleResult(task)
        }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account = task.result
            if(account != null){
                setUI(account)
            }else{
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    private fun setUI(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credentials).addOnCompleteListener {
            if(it.isSuccessful){
                if(account.idToken != null){
                    authViewModel.doLoginWithGoogle(account.idToken!!)
                    Log.d("googles",account.idToken.toString())
                    authViewModel.doLoginWithGoogleObserver().observe(this){ loginGoogle ->
                        if(loginGoogle != null){
                            startActivity(Intent(this,VerificationActivity::class.java).also { its ->
                                its.putExtra("email",account.email)
                            })
                        }else{
                            authViewModel.messageObserver().observe(this){ message ->
                                Toast.makeText(this,message , Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                Log.d("ID_google",account.id.toString())
            }
        }
    }

    private fun signInGoogle() {
        val signIn = googleSignInClient.signInIntent
        launcher.launch(signIn)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}