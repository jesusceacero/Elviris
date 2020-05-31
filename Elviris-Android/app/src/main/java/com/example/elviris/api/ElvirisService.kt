package com.example.elviris.api

import com.example.damkeep.api.response.LoginDTO
import com.example.damkeep.api.response.LoginResponse
import com.example.elviris.api.response.CreateUserDTO
import com.example.elviris.api.response.Evento
import com.example.elviris.api.response.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ElvirisService {

    @POST("/auth/login")
    suspend fun login(@Body loginDTO: LoginDTO): Response<LoginResponse>

    @POST("/user/register")
    suspend fun register(@Body createUserDTO: CreateUserDTO) : Response<User>

    @GET("/eventos/")
    suspend fun eventos() : Response<List<Evento>>

    @GET("/user/me")
    suspend fun userLogueado() : Response<User>

    @POST("/eventos/reserva/add/{id}")
    suspend fun resrevarEvento(@Path("id")id: String) : Response<User>

    @GET("/eventos/reserva/me")
    suspend fun eventosReservados() : Response<List<Evento>>

    @POST("/eventos/reserva/del/{id}")
    suspend fun cancelarReserva(@Path("id")id: String) : Response<Evento>
}