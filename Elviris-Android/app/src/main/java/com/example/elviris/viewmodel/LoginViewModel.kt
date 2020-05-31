package com.example.elviris.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damkeep.api.response.LoginDTO
import com.example.damkeep.api.response.LoginResponse
import com.example.elviris.common.Resource
import com.example.elviris.repository.ElvirisReposirory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginViewModel @Inject constructor(
    private val elvirisReposirory: ElvirisReposirory
) : ViewModel(){

    var login : MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    fun login (loginDTO: LoginDTO) = viewModelScope.launch {
        login.value = Resource.Loading()
        delay(3000)
        val response = elvirisReposirory.login(loginDTO)
        login.value = handleLoginResponse(response)
    }

    private fun handleLoginResponse(response: Response<LoginResponse>) : Resource<LoginResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}