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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserViewModel @Inject constructor(
    private val elvirisReposirory: ElvirisReposirory
): ViewModel() {

    var user : MutableLiveData<Resource<User>> = MutableLiveData()
    var reserva : MutableLiveData<Resource<User>> = MutableLiveData()
    var usuariosEvento : MutableLiveData<Resource<List<User>>> = MutableLiveData()
    var editFoto : MutableLiveData<Resource<User>> = MutableLiveData()

    fun userCargar () = viewModelScope.launch {
        user.value = Resource.Loading()
        val response = elvirisReposirory.userLogueado()
        user.value = handleObjectResponse(response)
    }


    fun usuariosEventoCargar(id: String) = viewModelScope.launch {
        usuariosEvento.value = Resource.Loading()
        val response = elvirisReposirory.usuariosEvento(id)
        usuariosEvento.value = handleLsitResponse(response)
    }

    fun reserverEvento(id: String) = viewModelScope.launch {
        reserva.value = Resource.Loading()
        val response = elvirisReposirory.reserverEvento(id)
        reserva.value = handleObjectResponse(response)
    }

    fun editFotoCargar(file: MultipartBody.Part?,id : RequestBody) = viewModelScope.launch {
        editFoto.value = Resource.Loading()
        val response = elvirisReposirory.editfoto(file,id)
        editFoto.value = handleObjectResponse(response)
    }

    private fun handleObjectResponse(response: Response<User>) : Resource<User> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleLsitResponse(response: Response<List<User>>) : Resource<List<User>> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}