package binar.finalproject.MyAirFare_admin.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.MyAirFare_admin.adapter.UserAdapter
import binar.finalproject.MyAirFare_admin.adapter.UserEmailAdapter
import binar.finalproject.MyAirFare_admin.databinding.ActivityUserBinding
import binar.finalproject.MyAirFare_admin.model.user.User
import binar.finalproject.MyAirFare_admin.model.user.UserEmail
import binar.finalproject.MyAirFare_admin.viewmodel.auth.AuthPreferencesViewModel
import binar.finalproject.MyAirFare_admin.viewmodel.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {
    private var _binding : ActivityUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var userEmailAdapter: UserEmailAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        setContentView(binding.root)
        getAllUser()
        keyEventSearch()
        back()
    }

    private fun getAllUser(){
        authPreferencesViewModel.getToken().observe(this){
            showLoading(true)
            if(it != null && it != "undefined"){
                userViewModel.getAllUser(it)
                userViewModel.getAllUserObserver().observe(this){ list ->
                    if(list != null){
                        showLoading(false)
                        setRecyclerUser(list)
                    }else{
                        userViewModel.messageObserver().observe(this){message ->
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerUser(datas : MutableList<User>){
        userAdapter = UserAdapter(object : UserAdapter.OnClick{
            override fun onClicked(user: User) {
                startActivity(Intent(this@UserActivity,DeleteUserActivity::class.java).also{
                    it.putExtra("ids",user.id)
                })
            }
        })

        userAdapter.submitData(datas)
        binding.recyclerUser.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@UserActivity)
        }
    }

    private fun setRecyclerUserEmail(datas : MutableList<UserEmail>){
        userEmailAdapter = UserEmailAdapter(object : UserEmailAdapter.OnClick{
            override fun onClicked(user: UserEmail) {
                startActivity(Intent(this@UserActivity,DeleteUserActivity::class.java).also{
                    it.putExtra("ids",user.id)
                })
            }
        })

        userEmailAdapter.submitData(datas)
        binding.recyclerUser.apply {
            adapter = userEmailAdapter
            layoutManager = LinearLayoutManager(this@UserActivity)
        }
    }

    private fun showLoading(show : Boolean){
        if(show) binding.loading.visibility = View.VISIBLE else  binding.loading.visibility = View.GONE
    }
    private fun showFound(found : Boolean){
        binding.apply {
            if(found) {
                imageNotFound.visibility =  View.GONE
                tvNotFound.visibility =  View.GONE
            }else {
                imageNotFound.visibility =  View.GONE
                tvNotFound.visibility  = View.GONE
            }
        }
    }


    private fun searchUser(token : String,key : String){
        userViewModel.filterUser(token,key)
        showLoading(true)
        binding.recyclerUser.visibility = View.GONE
        userViewModel.filterUserObserver().observe(this){
            if(it != null){
                if(it.size > 0){
                    showLoading(false)
                    showFound(true)
                    binding.recyclerUser.visibility = View.VISIBLE
                    setRecyclerUserEmail(it)
                }else{
                    showLoading(false)
                    showFound(false)
                }
            }else{
               userViewModel.messageObserver().observe(this){message ->
                   Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
               }
            }
        }
    }

    private fun keyEventSearch(){
        binding.etEmail.setOnEditorActionListener { _, actionId,_ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val search = binding.etEmail.text.toString()
                if(search.isNotBlank()){
                    authPreferencesViewModel.getToken().observe(this){
                        if(it != null && it != "undefined"){
                            searchUser(it,search)
                        }
                    }
                }else{
                    Toast.makeText(this, "Kolom search tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
                true
            } else false
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