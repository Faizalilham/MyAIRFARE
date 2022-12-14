@file:Suppress("UnnecessaryVariable", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused"
)

package binar.finalproject.MyAirFare.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.MyAirFare.adapter.NotificationsAdapter
import binar.finalproject.MyAirFare.databinding.FragmentNotificationBinding
import binar.finalproject.MyAirFare.ui.activities.LoginActivity
import binar.finalproject.MyAirFare.utils.Notifications
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import binar.finalproject.MyAirFare.viewmodel.CurrentUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject


@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var  binding : FragmentNotificationBinding
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    private lateinit var currentUserViewModel : CurrentUserViewModel
    private lateinit var socket : Socket
    private lateinit var notificationsAdapter : NotificationsAdapter
    private val dataNotifications = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        currentUserViewModel = ViewModelProvider(this)[CurrentUserViewModel::class.java]
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        getNotifications()

    }

    private fun doLogin(){
        binding.btnLogin.setOnClickListener { startActivity(
            Intent(requireActivity(),
                LoginActivity::class.java)
        )}
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun connectSocket(id : String){
        socket = IO.socket("https://binarfinalsocketserver-production-1a1f.up.railway.app/")
        socket.connect()
        showLoading(true)
        socket.emit("newUser",id)
        socket.on("notify"){
            Log.d("Notification","$it")
            var a = ""
            if(it != null){
                for (obj in it) {
                    val strArray = obj
                    dataNotifications.add(strArray.toString())
                    Log.d("NOTIFIKASI","$dataNotifications")
                    a = dataNotifications.last()
                }
                var dt = listOf<String>()
                if(dataNotifications.isNotEmpty()){
                    dt = dataNotifications.distinct()
                }
                activity?.runOnUiThread{
                    showWarning(false)
                    showLoading(false)
                    setRecycler(dt.toMutableList())
                    Log.d("DATAS","${dataNotifications.size}")
                }
                if(dataNotifications.size > 10){
                    Log.d("DATAS","${dataNotifications.slice(dataNotifications.size-1 downTo dataNotifications.size -5)}")
                    Notifications.makeStatusNotification(a,requireContext(), dataNotifications.slice(
                        dataNotifications.size-5 until dataNotifications.size
                    ).toMutableList())
                }

            }else{
                activity?.runOnUiThread {
                    showWarning(true)
                    showLoading(false)
                }
            }
        }
    }


    private fun showLoading(loading: Boolean){
        if(loading) binding.loading.visibility = View.VISIBLE else binding.loading.visibility = View.GONE
    }


    private fun showWarning(show : Boolean){
        binding.apply {
            if(show){
                imageNotFound.visibility = View.VISIBLE
                tvNotFound.visibility = View.VISIBLE
            }else{
                imageNotFound.visibility = View.GONE
                tvNotFound.visibility = View.GONE
            }
        }
    }

    private fun setRecycler(data : MutableList<String>){
        notificationsAdapter = NotificationsAdapter()
        notificationsAdapter.submitData(data)
        binding.recyclerNotifications.apply {
            adapter = notificationsAdapter
            if(isAdded && activity != null){
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getNotifications(){
        authPreferencesViewModel.getToken().observe(requireActivity()){
            if(it != null && it != "undefined"){
                currentUserViewModel.currentUser(it)
                currentUserViewModel.currentUserObserver().observe(requireActivity()){user ->
                    if(user != null){
                        connectSocket(user.user.id)
                    }
                }
            }
        }
    }


    private val onNewMessage =
        Emitter.Listener { args ->
            requireActivity().runOnUiThread(Runnable {
                val data = args[0] as JSONObject
                Log.d("DATAS","$data")
                try {
                    val datas = data.getString("")
                    Toast.makeText(requireActivity(), datas, Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    return@Runnable
                }
            })
        }

    private fun setupView(){
        authPreferencesViewModel.getToken().observe(requireActivity()){
            if(it != null && it != "undefined"){
                nonGuest()
            }else{
                guest()
                showLoading(false)
            }
        }
    }

    private fun guest(){
        binding.apply {
            imageNotFound.visibility = View.GONE
            tvNotFound.visibility = View.GONE
            recyclerNotifications.visibility = View.GONE
            imageGuest.visibility = View.VISIBLE
            tvGuest.visibility = View.VISIBLE
            btnLogin.visibility = View.VISIBLE
            doLogin()
        }
    }

    private fun nonGuest(){
        binding.apply {
            imageNotFound.visibility = View.VISIBLE
            tvNotFound.visibility = View.VISIBLE
            recyclerNotifications.visibility = View.VISIBLE
            imageGuest.visibility = View.GONE
            tvGuest.visibility = View.GONE
            btnLogin.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        dataNotifications.clear()
    }





}