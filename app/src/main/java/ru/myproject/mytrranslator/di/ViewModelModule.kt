package ru.myproject.mytrranslator.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.myproject.mytrranslator.view.main.MainViewModel

// Модуль послужит источником коллекции ViewModel’ей для фабрики:
// - мы используем этот модуль для создания ViewModel
// - мы предоставляем ключ для каждой новой ViewModel при помощи класса ViewModelKey, созданного выше;
// - и уже в Activity мы используем фабрику для создания нужной нам ViewModel
@Module(includes = [InteractorModule::class])
internal class ViewModelModule {

    // Фабрика
    @Provides
    internal fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory = factory

    // Этот метод просто говорит Dagger’у: помести эту модель в список (map) моделей,
    // используя аннотацию @IntoMap, где в качестве ключа будет класс MainViewModel::class
    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal fun mainViewModel(mainViewModel: MainViewModel): ViewModel = mainViewModel
}