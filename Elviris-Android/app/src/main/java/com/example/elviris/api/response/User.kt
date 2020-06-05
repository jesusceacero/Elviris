package com.example.elviris.api.response

import java.util.*

data class User(
    var username : String,
    var fullName : String,
    var roles : String,
    var eventos : List<String>,
    var foto : String?,
    var id : UUID
)