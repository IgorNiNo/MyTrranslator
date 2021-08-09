package ru.myproject.mytrranslator.application

import android.app.Application
import org.koin.core.context.startKoin
import ru.myproject.mytrranslator.di.application
import ru.myproject.mytrranslator.di.mainScreen

class TranslatorApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(application, mainScreen))
        }
    }
}