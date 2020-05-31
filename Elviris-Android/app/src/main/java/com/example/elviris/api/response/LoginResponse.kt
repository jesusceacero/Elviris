package com.example.damkeep.api.response

import com.example.elviris.api.response.User

data class LoginResponse (
    val token : String,
    val user : User
)