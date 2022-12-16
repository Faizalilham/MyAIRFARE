package binar.finalproject.MyAirFare.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.MyAirFare.databinding.FragmentNotificationBinding
import binar.finalproject.MyAirFare.socket.SocketHandler
import binar.finalproject.MyAirFare.ui.activities.LoginActivity
import binar.finalproject.MyAirFare.utils.Notifications
import binar.finalproject.MyAirFare.viewmodel.AuthPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject


@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private var _binding : FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var authPreferencesViewModel: AuthPreferencesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(layoutInflater)
        authPreferencesViewModel = ViewModelProvider(this)[AuthPreferencesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        connectSocket()

    }
    private fun doLogin(){
        binding.btnLogin.setOnClickListener { startActivity(
            Intent(requireActivity(),
                LoginActivity::class.java)
        )}
    }

    private fun connectSocket(){
        SocketHandler.setSocket()
        val socket = SocketHandler.getSocket()
        socket.connect()
        socket.emit("counter") // endpoint request notifikasi
        socket.on("counter"){  // get response data
            if(it != null){
                val count = it[0] as String// getData from server
                activity?.runOnUiThread{
                    Notifications.makeStatusNotification(count,requireActivity())
                    Toast.makeText(requireActivity(), count, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private val onNewMessage =
        Emitter.Listener { args ->
            requireActivity().runOnUiThread(Runnable {
                val data = args[0] as JSONObject
                val datas: String
                try {
                    datas = data.getString("")
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}