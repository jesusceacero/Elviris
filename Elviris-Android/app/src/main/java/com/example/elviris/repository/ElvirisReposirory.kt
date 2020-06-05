package com.example.elviris.repository

import com.example.damkeep.api.response.LoginDTO
import com.example.elviris.api.ElvirisService
import com.example.elviris.api.response.CreateUserDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.Part
import java.util.*
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

    suspend fun eventoId (id : String) = elvirisService.eventoId(id)

    suspend fun usuariosEvento(id : String) = elvirisService.usuariosEvento(id)

    suspend fun addEventos(file: MultipartBody.Part?, e : RequestBody) = elvirisService.addEvento(file,e)

    suspend fun editEventos(file: MultipartBody.Part?, e : RequestBody) = elvirisService.editEvento(file,e)

    suspend fun deleteEvento(id : String) = elvirisService.deleteEvento(id)

    suspend fun editfoto(file: MultipartBody.Part?,id : RequestBody) = elvirisService.editFoto(file,id)
}
