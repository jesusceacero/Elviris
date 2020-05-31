package com.example.elviris.di

import android.app.Application

class MyApp: Application() {
    //TODO definimos el @Component para tener acceso a las dependencias en la aplicación
    val appComponent: ApplicationComponent = DaggerApplicationComponent.create()

    companion object {
        lateinit var instance: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}