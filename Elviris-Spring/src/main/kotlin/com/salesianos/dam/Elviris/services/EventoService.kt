package com.salesianos.dam.Elviris.services

import com.salesianos.dam.Elviris.DTOs.EventoDTO
import com.salesianos.dam.Elviris.DTOs.toEventoDTO
import com.salesianos.dam.Elviris.model.Evento
import com.salesianos.dam.Elviris.model.MyUser
import com.salesianos.dam.Elviris.repository.EventoRepository
import com.salesianos.dam.Elviris.upload.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.net.URL
import java.util.*

@Service
class EventoService (
        private val eventoRepository: EventoRepository,
        private val imageStorageService: ImgurStorageService,
        private val imgurService: ImgurService
){

    fun eventoID(id : UUID) = eventoRepository.eventoID(id)

    fun findById(id : UUID) = eventoRepository.findById(id)

    fun deleteById(id:UUID) = eventoRepository.deleteById(id)

    fun ordenadas(): List<EventoDTO>{
        var list = eventoRepository.ordenadas().map { e -> e.toEventoDTO()}

        for (e in list){
            if (e.foto != null){
                e.foto = getUrl(e.foto!!).get().toString()
            }
        }
        return list
    }

    fun save(e : Evento) = eventoRepository.save(e)

    fun eventosReservados(u : MyUser) : List<EventoDTO> {
        var list =eventoRepository.eventosReservados(u).map { even -> even.toEventoDTO() }

        for (e in list){
            if (e.foto != null){
                e.foto = getUrl(e.foto!!).get().toString()
            }
        }
        return list

    }

    fun addEvento(e : EventoDTO, file : MultipartFile?) : EventoDTO{
        var e = Evento(e.titulo,e.descripcion,e.aforo,e.fecha)
        var imageAttribute : Optional<ImgurImageAttribute> = Optional.empty()
        if (file != null){
            if (!file.isEmpty) {
                imageAttribute = imageStorageService.store(file)
                e.foto = imageAttribute.orElse(null)
            }
        }
        var dto = eventoRepository.save(e).toEventoDTO()
        if(e.foto !=null){
            var url = getUrl(e.foto?.id.toString())
            dto.foto = url.get().toString()
        }
        return dto
    }

    fun editEvento(edit : EventoDTO, file : MultipartFile?) : EventoDTO{
        var e = eventoRepository.eventoID(edit.id!!)
        e.titulo = edit.titulo
        e.descripcion = edit.descripcion
        e.aforo = edit.aforo
        e.fecha = edit.fecha

        var imageAttribute : Optional<ImgurImageAttribute> = Optional.empty()
        if (file != null) {
            if (!file.isEmpty){
                imageAttribute = imageStorageService.store(file!!)
                e.foto = imageAttribute.orElse(null)
            }
        }
        var dto = eventoRepository.save(e).toEventoDTO()
        if(e.foto !=null){
            var url = getUrl(e.foto?.id.toString())
            dto.foto = url.get().toString()
        }
        return dto
    }

    fun getUrl(id : String) : Optional<URL> {
        var resource: Optional<MediaTypeUrlResource>
        try {
            resource = imageStorageService.loadAsResource(id)
            if (resource.isPresent) {
                return Optional.of(resource.get().url)
            }
            return Optional.empty()
        }catch (ex: ImgurImageNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada")
        }
    }
    fun eventoURL(e : EventoDTO): EventoDTO{
        if (e.foto != null){
            e.foto = getUrl(e.foto!!).get().toString()
        }
        return e    }
}