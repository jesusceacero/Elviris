package com.salesianos.dam.Elviris.repository


import com.salesianos.dam.Elviris.model.Evento
import com.salesianos.dam.Elviris.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {

    @Query("select distinct u from User u left join fetch u.eventos where u.id = :id")
    fun usuarioID(id : UUID) : User


    fun findByUsername(username : String) : Optional<User>

}