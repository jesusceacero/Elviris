package com.example.elviris.api.response

import org.joda.time.LocalDate

data class Evento(
    var titulo : String,
    var descripcion : String,
    var aforo : Int,
    var fecha : String,
    var foto : String? = null,
    var usuarios : List<String> = ArrayList(),
    var id : String? = null
)