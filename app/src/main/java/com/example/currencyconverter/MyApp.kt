package com.example.currencyconverter

import android.app.Application
import com.example.currencyconverter.module.clientModule
import com.example.currencyconverter.module.serviceModule
import com.example.currencyconverter.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application()  {


    override fun onCreate()
    {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp) // access to your application
            modules(listOf(viewModelModule, clientModule, serviceModule)) //you can add list of modules here
        }
    }

}