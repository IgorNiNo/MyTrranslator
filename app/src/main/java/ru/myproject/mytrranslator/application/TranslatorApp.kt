package ru.myproject.mytrranslator.application

import android.app.Application
import ru.myproject.mytrranslator.di.DaggerAppComponent

class TranslatorApp : Application() {

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .build()
            .inject(this)
    }
}