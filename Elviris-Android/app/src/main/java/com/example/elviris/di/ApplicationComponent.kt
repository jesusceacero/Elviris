package com.example.elviris.di


import com.example.elviris.*
import com.example.elviris.api.NetworkModule
import com.example.elviris.ui.eventos.EventosFragment
import com.example.elviris.ui.perfil.PerfilFragment
import com.example.elviris.ui.reservas.ReservasFragment
import com.example.elviris.ui.user.UserFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component( modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(loginActivity: LoginActivity)
    fun inject(registerActivity: RegisterActivity)
    fun inject(eventosFragment: EventosFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(reservasFragment: ReservasFragment)
    fun inject(perfilFragment: PerfilFragment)
    fun inject(detalleEventoActivity: DetalleEventoActivity)
    fun inject(userFragment: UserFragment)
    fun inject(addPutEventoActivity: AddPutEventoActivity)

}