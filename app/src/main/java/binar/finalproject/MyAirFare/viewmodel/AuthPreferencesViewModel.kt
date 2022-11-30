package binar.finalproject.MyAirFare.viewmodel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import binar.finalproject.MyAirFare.datastore.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthPreferencesViewModel @Inject constructor(@ApplicationContext context :Context) : ViewModel() {

    private val authPreferences = AuthPreferences(context)


    fun setToken(token : String){
        viewModelScope.launch {
            authPreferences.setToken(token)
        }
    }

    fun getToken () = authPreferences.getToken().asLiveData()

    fun deleteToken(){
        viewModelScope.launch {
            authPreferences.deleteToken()
        }
    }
}