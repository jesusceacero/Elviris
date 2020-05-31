package com.example.elviris.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damkeep.api.response.LoginDTO
import com.example.damkeep.api.response.LoginResponse
import com.example.elviris.api.response.CreateUserDTO
import com.example.elviris.api.response.User
import com.example.elviris.common.Resource
import com.example.elviris.repository.ElvirisReposirory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterViewModel @Inject constructor( private val elvirisReposirory: ElvirisReposirory
) : ViewModel() {

    var user : MutableLiveData<Resource<User>> = MutableLiveData()

    fun register (createUserDTO: CreateUserDTO) = viewModelScope.launch {
        user.value = Resource.Loading()
        delay(3000)
        val response = elvirisReposirory.register(createUserDTO)
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
}