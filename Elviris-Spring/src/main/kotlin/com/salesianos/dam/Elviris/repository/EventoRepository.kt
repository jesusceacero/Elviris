package com.salesianos.dam.Elviris.repository

import com.salesianos.dam.Elviris.model.Evento
import com.salesianos.dam.Elviris.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*
import javax.annotation.PostConstruct

interface EventoRepository : JpaRepository<Evento, UUID> {

    @Query("select distinct e from Evento e left join fetch e.usuarios where e.id = :id")
    fun eventoID(id : UUID) : Evento

    @Query("select e from Evento e left join fetch e.usuarios Order by e.fecha desc")
    fun ordenadas(): List<Evento>

    @Query("select e from Evento e left join fetch e.usuarios where :user MEMBER OF e.usuarios")
    fun eventosReservados(user:User) : List<Evento>

    @Query("select e from Evento e left join fetch e.usuarios")
    fun findByUsuariosContaining(user:User) : List<Evento>
}

@Component
class InitDataComponent(
        val eventoRepository: EventoRepository,
        val userRepository: UserRepository,
        val encoder: PasswordEncoder
){
    @PostConstruct
    fun initData(){

        val evento1 = Evento("Sabado Noche",
                "Podras disfritar de la mejor musica y el mejor ambiente.",
                50,
                LocalDate.of(2020,7,15),
                "nada")
        eventoRepository.save(evento1)

        val evento2 = Evento("Sabado Noche",
                "Podras disfritar de la mejor musica y el mejor ambiente.",
                50,
                LocalDate.of(2020,7,10),
                "nada")
        eventoRepository.save(evento2)

        val evento3 = Evento("Sabado Noche",
                "Podras disfritar de la mejor musica y el mejor ambiente.",
                50,
                LocalDate.of(2020,6,15),
                "nada")
        eventoRepository.save(evento3)

        val user = User("c@gmail.com",encoder.encode("123456"),"Jesus Ceacero Jimeno","USER")
        userRepository.save(user)

    }
}