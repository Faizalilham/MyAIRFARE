package binar.finalproject.MyAirFare.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.R
import binar.finalproject.MyAirFare.databinding.ActivityLoginBinding
import binar.finalproject.MyAirFare.utils.AuthValidation
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
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
class LoginActivity : AppCompatActivity() {
    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel : AuthViewModel
    private lateinit var authPreferences : AuthPreferencesViewModel
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        authPreferences = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(
            R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        setContentView(binding.root)
        doRegister()
        doLogin()
        doResetPassword()
        doSignInWithGoogle()
    }

    private fun doRegister(){
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

    private fun doResetPassword(){
        binding.tvForgetPassword.setOnClickListener {
            startActivity(Intent(this,ForgetPasswordActivity::class.java))
        }
    }

    private fun doSignInWithGoogle(){
        binding.cardGoogle.setOnClickListener {
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
            }
        }else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    private fun setUI(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken,null)
        Toast.makeText(this, credentials.toString(), Toast.LENGTH_SHORT).show()
        auth.signInWithCredential(credentials).addOnCompleteListener {
            if(it.isSuccessful){
                if(account.idToken != null){
                    authViewModel.doLoginWithGoogle(account.idToken!!)
                    authViewModel.doLoginWithGoogleObserver().observe(this){ loginGoogle ->
                        if(loginGoogle != null){
                            startActivity(Intent(this,MainActivity::class.java).also { _ ->
                                Log.d("touken",loginGoogle.token)
                                Toast.makeText(this, "Login success, hallo ${loginGoogle.user.username}", Toast.LENGTH_SHORT).show()
                                authPreferences.setToken(loginGoogle.token)
                                finish()

                            })
                        }else{
                            authViewModel.messageObserver().observe(this){ message ->
                                Toast.makeText(this,message , Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                Toast.makeText(this, account.idToken, Toast.LENGTH_SHORT).show()
                Log.d("ID_google",account.id.toString())
            }
        }
    }

    private fun signInGoogle() {
        val signIn = googleSignInClient.signInIntent
        launcher.launch(signIn)
    }

    private fun doLogin(){
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val validation = AuthValidation.loginValidation(email,password)
            if(validation == "success"){
                authViewModel.doLogin(email,password)
                authViewModel.doLoginObserver().observe(this){
                    if(it != null){
                        startActivity(Intent(this,MainActivity::class.java).also { _ ->
                            Log.d("touken",it.token)
                            Toast.makeText(this, "Login success, hallo ${it.user.username}", Toast.LENGTH_SHORT).show()
                            authPreferences.setToken(it.token)
                            finish()

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}