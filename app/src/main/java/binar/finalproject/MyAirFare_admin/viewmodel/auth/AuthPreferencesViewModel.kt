package binar.finalproject.MyAirFare_admin.viewmodel.auth


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
fun doLoginWithGoogle(credential : String){
        api.doLoginWithGoole(LoginGoogle(credential)).enqueue(object : Callback<UserRegisterResponse>{
            override fun onResponse(
                call: Call<UserRegisterResponse>,
                response: Response<UserRegisterResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        doLoginWithGoogle.postValue(body)
                        Log.d("Success","$body")
                    }else{
                        message.postValue(response.message())
                        doLoginWithGoogle.postValue(null)
                        Log.d("Response Error","Response Error => ${response.message()}")
                    }
                }else{
                    doLoginWithGoogle.postValue(null)
                    message.postValue(response.body().toString())
                    Log.d("Response Error ygy","Response Error => ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                message.postValue(t.message)
                doLoginWithGoogle.postValue(null)
                Log.d("Response Error","Response Error => ${t.message}")
            }

        })
    }
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