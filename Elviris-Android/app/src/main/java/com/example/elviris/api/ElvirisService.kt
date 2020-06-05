package com.example.elviris.api

import com.example.damkeep.api.response.LoginDTO
import com.example.damkeep.api.response.LoginResponse
import com.example.elviris.api.response.CreateUserDTO
import com.example.elviris.api.response.Evento
import com.example.elviris.api.response.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

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

    @GET("/eventos/{id}")
    suspend fun eventoId(@Path("id") id : String) : Response<Evento>

    @GET("/user/evento/{id}")
    suspend fun usuariosEvento(@Path("id") id : String) : Response<List<User>>

    @Multipart
    @POST("/eventos/add")
    suspend fun addEvento(@Part file: MultipartBody.Part?,
                          @Part("nuevo") e : RequestBody) : Response<Evento>


    @Multipart
    @PUT("/eventos/edit")
    suspend fun editEvento(@Part file: MultipartBody.Part?,
                          @Part("edit") e : RequestBody) : Response<Evento>

    @DELETE("/eventos/del/{id}")
    suspend fun deleteEvento(@Path("id") id : String) : Response<Void>

    @Multipart
    @PUT("/user/foto")
    suspend fun editFoto(@Part file: MultipartBody.Part?,
                         @Part("id") id : RequestBody) : Response<User>
}