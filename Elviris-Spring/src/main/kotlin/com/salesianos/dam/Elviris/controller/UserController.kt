package com.salesianos.dam.Elviris.controller


import com.salesianos.dam.Elviris.DTOs.*
import com.salesianos.dam.Elviris.model.User
import com.salesianos.dam.Elviris.repository.EventoRepository
import com.salesianos.dam.Elviris.repository.UserRepository
import com.salesianos.dam.Elviris.services.EventoService
import com.salesianos.dam.Elviris.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.util.*
import java.util.stream.Collectors

@Controller
@RequestMapping("/user")
class UserController(
        val userService: UserService,
        val eventoService: EventoService,
        private val encoder: PasswordEncoder
) {

    @PostMapping("/register")
    fun nuevoUsuario(@RequestBody newUser : CreateUserDTO): ResponseEntity<UserDTO> =
            userService.create(newUser).map { ResponseEntity.status(HttpStatus.CREATED).body(it.toUserDTO()) }.orElseThrow {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ${newUser.username} ya existe")
            }

    @GetMapping("/evento/{id}")
    fun usuariosEvento(@PathVariable id : UUID) : ResponseEntity<List<UserDTO>>{
        var e = eventoService.eventoID(id)
        println(e.descripcion)
        return  ResponseEntity.status(HttpStatus.OK).body(userService.usuariosEvento(e).stream().distinct().collect(Collectors.toList()))
    }

    @PutMapping("/foto")
    fun editFoto(@RequestPart("id") id : IdDTO,
                 @RequestPart("file") file : MultipartFile
    ) : ResponseEntity<UserDTO> {
        println(id)
        return ResponseEntity.status(HttpStatus.OK).body(userService.editfoto(id,file))
    }
}