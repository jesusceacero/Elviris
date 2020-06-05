package com.example.elviris.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damkeep.api.response.LoginDTO
import com.example.elviris.api.response.Evento
import com.example.elviris.common.Resource
import com.example.elviris.repository.ElvirisReposirory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventosViewModel @Inject constructor(
    private val elvirisReposirory: ElvirisReposirory
):ViewModel(){

    var eventos : MutableLiveData<Resource<List<Evento>>> = MutableLiveData()
    var reservados: MutableLiveData<Resource<List<Evento>>> = MutableLiveData()
    var cancelada : MutableLiveData<Resource<Evento>> = MutableLiveData()
    var evendoDetalle : MutableLiveData<Resource<Evento>> = MutableLiveData()
    var addEvento : MutableLiveData<Resource<Evento>> = MutableLiveData()
    var editEvento : MutableLiveData<Resource<Evento>> = MutableLiveData()
    var deleteEvento: MutableLiveData<Resource<Void>> = MutableLiveData()

    fun eventosCargar () = viewModelScope.launch {
        eventos.value = Resource.Loading()
        val response = elvirisReposirory.eventos()
        eventos.value = handlelistResponse(response)
    }

    fun eventosReservados () = viewModelScope.launch {
        reservados.value = Resource.Loading()
        val response = elvirisReposirory.eventosReservados()
        reservados.value = handlelistResponse(response)
    }

    fun cancelarReserva (id : String) = viewModelScope.launch {
        cancelada.value = Resource.Loading()
        val response = elvirisReposirory.cancelarReserva(id)
        cancelada.value = handleObjectResponse(response)
    }

    fun eventoId (id : String) = viewModelScope.launch {
        evendoDetalle.value = Resource.Loading()
        val response = elvirisReposirory.eventoId(id)
        evendoDetalle.value = handleObjectResponse(response)
    }

    fun addEventoCargar (file:MultipartBody.Part? , e : RequestBody) = viewModelScope.launch {
        addEvento.value = Resource.Loading()
        val response = elvirisReposirory.addEventos(file,e)
        addEvento.value = handleObjectResponse(response)
    }

    fun editEventoCargar (file:MultipartBody.Part? , e : RequestBody) = viewModelScope.launch {
        editEvento.value = Resource.Loading()
        val response = elvirisReposirory.editEventos(file,e)
        editEvento.value = handleObjectResponse(response)
    }

    fun deleteEventoCargar (id : String) = viewModelScope.launch {
        deleteEvento.value = Resource.Loading()
        val response = elvirisReposirory.deleteEvento(id)
        deleteEvento.value = handleDeleteResponse(response)
    }

    private fun handlelistResponse(response: Response<List<Evento>>) : Resource<List<Evento>> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleObjectResponse(response: Response<Evento>) : Resource<Evento> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleDeleteResponse(response: Response<Void>) : Resource<Void> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}