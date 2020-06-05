package com.salesianos.dam.Elviris.model

import com.salesianos.dam.Elviris.upload.ImgurImageAttribute
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
data class Evento(
        var titulo : String,
        var descripcion : String,
        var aforo : Int,
        var fecha : LocalDate,
        var foto : ImgurImageAttribute? = null,
        @Id @GeneratedValue val id: UUID? = null,
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "evento_user",
                joinColumns = [JoinColumn(name = "evento_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
        var usuarios : MutableList<MyUser> = mutableListOf()
) {
        fun addUser(u: MyUser) {
                usuarios.add(u)
        }

        fun deleteUser(u: MyUser) {
                usuarios.remove(u)
        }
}