package com.salesianos.dam.Elviris.DTOs

import com.salesianos.dam.Elviris.model.MyUser
import java.util.*

data class UserDTO (
        var username : String,
        var fullName: String,
        var roles: String,
        var eventos : List<String>?,
        val id: UUID? = null,
        var foto : String? ? = null
)

fun MyUser.toUserDTO() : UserDTO{
    var lista : MutableList<String> = mutableListOf()
    for (i  in eventos){
        lista.add(i.id.toString())
    }
    return UserDTO(username, fullName, roles.joinToString(),lista, id,foto?.id)
}

fun MyUser.toUserLoginDTO() = UserDTO(username, fullName, roles.joinToString(), mutableListOf(), id,foto?.id)
