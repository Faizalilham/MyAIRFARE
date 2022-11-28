package binar.finalproject.MyAirFare.viewmodel

import android.content.Context
import androidx.lifecycle.*
import binar.finalproject.MyAirFare.datastore.OnBoardingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(@ApplicationContext context :Context):ViewModel() {

    private val onBoardingPreferences = OnBoardingPreferences(context)

    fun setBoardingKey(key : Boolean){
        viewModelScope.launch{
            onBoardingPreferences.setToken(key)
        }
    }

    fun getBoardingKey() = onBoardingPreferences.getToken().asLiveData()
}