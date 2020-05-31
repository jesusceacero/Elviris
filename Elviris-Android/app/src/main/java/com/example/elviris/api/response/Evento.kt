package com.example.elviris.api.response

import org.joda.time.LocalDate

data class Evento(
    var titulo : String,
    var descripcion : String,
    var aforo : Int,
    var fecha : String,
    var foto : String?,
    var usuarios : List<String>,
    var id : String
)