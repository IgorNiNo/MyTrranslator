package ru.myproject.mytrranslator.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.myproject.mytrranslator.view.main.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}