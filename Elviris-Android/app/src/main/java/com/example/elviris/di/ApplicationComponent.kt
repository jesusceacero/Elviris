package com.example.elviris.di


import com.example.elviris.LoginActivity
import com.example.elviris.MainActivity
import com.example.elviris.RegisterActivity
import com.example.elviris.api.NetworkModule
import com.example.elviris.ui.dashboard.DashboardFragment
import com.example.elviris.ui.eventos.EventosFragment
import com.example.elviris.ui.home.HomeFragment
import com.example.elviris.ui.reservas.ReservasFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component( modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(movieListFragment: HomeFragment)
    fun inject(dashboardFragment: DashboardFragment)
    fun inject(loginActivity: LoginActivity)
    fun inject(registerActivity: RegisterActivity)
    fun inject(eventosFragment: EventosFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(reservasFragment: ReservasFragment)

}