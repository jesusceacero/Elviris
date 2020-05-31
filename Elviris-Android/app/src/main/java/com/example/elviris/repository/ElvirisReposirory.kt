package com.example.elviris.repository

import com.example.damkeep.api.response.LoginDTO
import com.example.elviris.api.ElvirisService
import com.example.elviris.api.response.CreateUserDTO
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElvirisReposirory @Inject constructor(
    private var elvirisService: ElvirisService) {

    suspend fun login(loginDTO: LoginDTO) = elvirisService.login(loginDTO)

    suspend fun register(createUserDTO: CreateUserDTO) = elvirisService.register(createUserDTO)

    suspend fun eventos() = elvirisService.eventos()

    suspend fun userLogueado() = elvirisService.userLogueado()

    suspend fun reserverEvento(id : String) = elvirisService.resrevarEvento(id)

    suspend fun eventosReservados() = elvirisService.eventosReservados()

    suspend fun cancelarReserva(id : String) = elvirisService.cancelarReserva(id)
}
