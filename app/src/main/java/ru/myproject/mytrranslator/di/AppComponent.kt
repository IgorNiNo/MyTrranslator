package ru.myproject.mytrranslator.di

import dagger.Component
import ru.myproject.mytrranslator.application.TranslatorApp
import ru.myproject.mytrranslator.view.main.MainActivity
import javax.inject.Singleton

@Component(
    modules = [
        InteractorModule::class,
        RepositoryModule::class,
        ViewModelModule::class]
)

@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {

        fun build(): AppComponent
    }

    fun inject(englishVocabularyApp: TranslatorApp)

    fun inject(activity: MainActivity)
}