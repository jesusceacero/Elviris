package com.salesianos.dam.Elviris.services


import com.salesianos.dam.Elviris.DTOs.*
import com.salesianos.dam.Elviris.model.Evento
import com.salesianos.dam.Elviris.model.User
import com.salesianos.dam.Elviris.repository.UserRepository
import com.salesianos.dam.Elviris.upload.ImgurImageAttribute
import com.salesianos.dam.Elviris.upload.ImgurImageNotFoundException
import com.salesianos.dam.Elviris.upload.ImgurStorageService
import com.salesianos.dam.Elviris.upload.MediaTypeUrlResource
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.net.URL
import java.util.*

@Service
class UserService (
        private val repo: UserRepository,
        private val encoder: PasswordEncoder,
        private val imageStorageService: ImgurStorageService
) {

    fun create(newUser : CreateUserDTO): Optional<User> {
        if (findByUsername(newUser.username).isPresent)
            return Optional.empty()
        return Optional.of(
                with(newUser) {
                    repo.save(User(username, encoder.encode(password), fullName, "USER"))
                }

        )
    }

    fun findByUsername(username : String) = repo.findByUsername(username)

    fun findById(id : UUID) = repo.findById(id)

    fun usuariosId(id: UUID )= repo.usuarioID(id)

    fun usuariosEvento(e:Evento) : List<UserDTO>{
        var list = repo.usuariosEvento(e).map { even -> even.toUserDTO() }
        for (e in list){
            if (e.foto != null){
                e.foto = getUrl(e.foto!!).get().toURI().toString()
            }
        }
        return list
    }
    fun editfoto(id: IdDTO, file : MultipartFile) : UserDTO {
        var u = repo.usuarioID(id.id)

        var imageAttribute : Optional<ImgurImageAttribute> = Optional.empty()
        if (!file.isEmpty) {
            imageAttribute = imageStorageService.store(file)
        }

        u.foto = imageAttribute.orElse(null)
        var save = repo.save(u)
        var dto = repo.usuarioID(save.id!!).toUserDTO()
        var url = getUrl(imageAttribute.get().id.toString())
        dto.foto = url.get().toString()
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

    fun userURL(u : UserDTO): UserDTO{
        if (u.foto != null){
            u.foto = getUrl(u.foto!!).get().toString()
        }
        return u
    }

}