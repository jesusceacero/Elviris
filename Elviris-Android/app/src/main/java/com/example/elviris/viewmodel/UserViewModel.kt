package com.example.elviris.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elviris.api.response.Evento
import com.example.elviris.api.response.User
import com.example.elviris.common.Resource
import com.example.elviris.repository.ElvirisReposirory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserViewModel @Inject constructor(
    private val elvirisReposirory: ElvirisReposirory
): ViewModel() {

    var user : MutableLiveData<Resource<User>> = MutableLiveData()
    var reserva : MutableLiveData<Resource<User>> = MutableLiveData()

    fun userCargar () = viewModelScope.launch {
        user.value = Resource.Loading()
        delay(3000)
        val response = elvirisReposirory.userLogueado()
        user.value = handleLoginResponse(response)
    }

    private fun handleLoginResponse(response: Response<User>) : Resource<User> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun reserverEvento(id: String) = viewModelScope.launch {
        reserva.value = Resource.Loading()
        delay(3000)
        val response = elvirisReposirory.reserverEvento(id)
        reserva.value = handleLoginResponse(response)
    }
}