package com.salesianos.dam.Elviris.repository


import com.salesianos.dam.Elviris.model.Evento
import com.salesianos.dam.Elviris.model.MyUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepository : JpaRepository<MyUser, UUID> {

    @Query("select distinct u from MyUser u left join fetch u.eventos where u.id = :id")
    fun usuarioID(id : UUID) : MyUser

    @Query("select u from MyUser u left join fetch u.eventos where :evento MEMBER OF u.eventos")
    fun usuariosEvento(evento:Evento) : List<MyUser>


    fun findByUsername(username : String) : Optional<MyUser>

}