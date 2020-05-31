package com.salesianos.dam.Elviris.controller

import com.salesianos.dam.Elviris.DTOs.EventoDTO
import com.salesianos.dam.Elviris.DTOs.toEventoDTO
import com.salesianos.dam.Elviris.model.Evento
import com.salesianos.dam.Elviris.model.User
import com.salesianos.dam.Elviris.repository.EventoRepository
import com.salesianos.dam.Elviris.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/eventos")
class EventoController(
        val eventoRepository: EventoRepository,
        val userRepository: UserRepository
) {

    @GetMapping("/")
    fun todas(): List<EventoDTO> {
        var eventos : List<Evento>
        eventos = eventoRepository.ordenadas()
        if (eventos.isEmpty())
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No hay eventos guardados.")
        return eventos.map { it ->it.toEventoDTO() }
    }

    @PostMapping("/reserva/add/{id}")
    fun reservarEvento(@PathVariable id : UUID,
                       @AuthenticationPrincipal user : User):ResponseEntity<EventoDTO>{
        var e = eventoRepository.eventoID(id)
        print(e.toString())
//        if (e.isEmpty){
//            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el evento.")
//        }
        e.addUser(user)
        return ResponseEntity.status(HttpStatus.OK).body(eventoRepository.save(e).toEventoDTO())
    }

    @PostMapping("/reserva/del/{id}")
    fun eliminarReserva(@PathVariable id : UUID,
                       @AuthenticationPrincipal user : User): EventoDTO{
        var e = eventoRepository.eventoID(id)
//        if (e == null){
//            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el evento.")
//        }
        e.deleteUser(user)
        eventoRepository.save(e)
        return e.toEventoDTO()
    }

    @GetMapping("/reserva/me")
    fun eventosReservados(@AuthenticationPrincipal user : User): List<EventoDTO>{
        return eventoRepository.eventosReservados(user).map { even -> even.toEventoDTO() }
    }

}