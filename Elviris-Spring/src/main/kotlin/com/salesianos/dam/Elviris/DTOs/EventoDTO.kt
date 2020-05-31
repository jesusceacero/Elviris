package com.salesianos.dam.Elviris.DTOs

import com.fasterxml.jackson.annotation.JsonFormat
import com.salesianos.dam.Elviris.model.Evento
import com.salesianos.dam.Elviris.model.User
import java.time.LocalDate
import java.util.*

data class EventoDTO (
        var titulo : String,
        var descripcion : String,
        var aforo : Int,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        var fecha : LocalDate,
        var foto : String,
        var usuarios : List<String>,
        val id: UUID? = null
)

fun Evento.toEventoDTO() : EventoDTO {
        var lista : MutableList<String> = mutableListOf()
        for (i  in usuarios){
              lista.add(i.id.toString())
        }
        return EventoDTO(titulo,descripcion,aforo,fecha,foto,lista,id)
}