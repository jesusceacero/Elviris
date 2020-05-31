package com.salesianos.dam.Elviris.DTOs

import com.salesianos.dam.Elviris.model.Evento
import com.salesianos.dam.Elviris.model.User
import java.util.*

data class UserDTO (
        var username : String,
        var fullName: String,
        var roles: String,
        var eventos : List<String>?,
        val id: UUID? = null
)

fun User.toUserDTO() : UserDTO{
    var lista : MutableList<String> = mutableListOf()
    for (i  in eventos){
        lista.add(i.id.toString())
    }
    return UserDTO(username, fullName, roles.joinToString(),lista, id)
}

fun User.toUserLoginDTO() = UserDTO(username, fullName, roles.joinToString(), mutableListOf(), id)
