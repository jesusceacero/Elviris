package com.salesianos.dam.Elviris.controller

import com.salesianos.dam.Elviris.DTOs.EventoDTO
import com.salesianos.dam.Elviris.DTOs.UserDTO
import com.salesianos.dam.Elviris.DTOs.UsuarioEventoDTO
import com.salesianos.dam.Elviris.DTOs.toEventoDTO
import com.salesianos.dam.Elviris.model.Evento
import com.salesianos.dam.Elviris.model.User
import com.salesianos.dam.Elviris.repository.EventoRepository
import com.salesianos.dam.Elviris.repository.UserRepository
import com.salesianos.dam.Elviris.services.EventoService
import com.salesianos.dam.Elviris.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/eventos")
class EventoController(
        val eventoService: EventoService,
        val userService: UserService
) {

    @GetMapping("/")
    fun todas(): List<EventoDTO> {
        var eventos = eventoService.ordenadas().stream().distinct().collect(Collectors.toList())
        if (eventos.isEmpty())
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No hay eventos guardados.")
        return eventos
    }

    @PostMapping("/reserva/add/{id}")
    fun reservarEvento(@PathVariable id : UUID,
                       @AuthenticationPrincipal user : User):ResponseEntity<EventoDTO>{
        var e = eventoService.eventoID(id)
        var u = userService.usuariosId(user.id!!)
        e.addUser(u)
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.save(e).toEventoDTO())
    }

    @PostMapping("/reserva/del/{id}")
    fun eliminarReserva(@PathVariable id : UUID,
                        @AuthenticationPrincipal user : User): EventoDTO{
        var e = eventoService.eventoID(id)
        e.deleteUser(user)
        eventoService.save(e)
        return e.toEventoDTO()
    }

    @GetMapping("/reserva/me")
    fun eventosReservados(@AuthenticationPrincipal user : User): List<EventoDTO>{
        return eventoService.eventosReservados(user)
    }

    @GetMapping("/{id}")
    fun evendoID(@PathVariable id: UUID) : EventoDTO {
        var e = eventoService.eventoID(id)
        if (e == null){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento no encontrado")
        }
        return eventoService.eventoURL(e.toEventoDTO())
    }

    @PostMapping("/add")
    fun add(
            @RequestPart("nuevo") new : EventoDTO,
            @RequestPart("file") file : MultipartFile?
    ) : EventoDTO {
        return eventoService.addEvento(new,file)
    }

    @PutMapping("/edit")
    fun edit(@RequestPart("edit") edit : EventoDTO,
             @RequestPart("file") file : MultipartFile?
    ) : EventoDTO {
        return eventoService.editEvento(edit,file)
    }

    @DeleteMapping("/del/{id}")
    fun eliminarEvento(@PathVariable id : UUID) : ResponseEntity<Void> {
        eventoService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}