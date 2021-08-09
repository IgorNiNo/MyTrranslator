package ru.myproject.mytrranslator.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.myproject.mytrranslator.model.data.DataModel
import ru.myproject.mytrranslator.model.datasource.RetrofitImplementation
import ru.myproject.mytrranslator.model.datasource.RoomDataBaseImplementation
import ru.myproject.mytrranslator.model.repository.Repository
import ru.myproject.mytrranslator.model.repository.RepositoryImplementation
import ru.myproject.mytrranslator.view.main.MainInteractor
import ru.myproject.mytrranslator.view.main.MainViewModel

// Для удобства создадим две переменные: в одной находятся зависимости, используемые во всём приложении, во второй - зависимости конкретного экрана
val application = module {
    // Функция single сообщает Koin, что эта зависимость должна храниться в виде синглтона (в Dagger есть похожая аннотация)
    // Аннотация named выполняет аналогичную Dagger функцию
    single<Repository<List<DataModel>>>(named(NAME_REMOTE)) {
        RepositoryImplementation(RetrofitImplementation())
    }
    single<Repository<List<DataModel>>>(named(NAME_LOCAL)) {
        RepositoryImplementation(RoomDataBaseImplementation())
    }
}

// Функция factory сообщает Koin, что эту зависимость нужно создавать каждый раз заново, что как раз подходит для Activity и её компонентов.
val mainScreen = module {
    factory { MainInteractor(get(named(NAME_REMOTE)), get(named(NAME_LOCAL))) }
    factory { MainViewModel(get()) }
}

