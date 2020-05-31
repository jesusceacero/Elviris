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
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventosViewModel @Inject constructor(
    private val elvirisReposirory: ElvirisReposirory
):ViewModel(){

    var eventos : MutableLiveData<Resource<List<Evento>>> = MutableLiveData()
    var reservados: MutableLiveData<Resource<List<Evento>>> = MutableLiveData()
    var cancelada : MutableLiveData<Resource<Evento>> = MutableLiveData()

    fun eventosCargar () = viewModelScope.launch {
        eventos.value = Resource.Loading()
        delay(3000)
        val response = elvirisReposirory.eventos()
        eventos.value = handlelistResponse(response)
    }

    fun eventosReservados () = viewModelScope.launch {
        reservados.value = Resource.Loading()
        delay(3000)
        val response = elvirisReposirory.eventosReservados()
        reservados.value = handlelistResponse(response)
    }

    fun cancelarReserva (id : String) = viewModelScope.launch {
        cancelada.value = Resource.Loading()
        delay(3000)
        val response = elvirisReposirory.cancelarReserva(id)
        cancelada.value = handleObjectResponse(response)
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
}