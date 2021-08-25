package ru.myproject.mytrranslator.di

import androidx.room.Room
import org.koin.dsl.module
import ru.myproject.mytrranslator.model.data.DataModel
import ru.myproject.mytrranslator.model.datasource.RetrofitImplementation
import ru.myproject.mytrranslator.model.datasource.RoomDataBaseImplementation
import ru.myproject.mytrranslator.model.repository.Repository
import ru.myproject.mytrranslator.model.repository.RepositoryImplementation
import ru.myproject.mytrranslator.model.repository.RepositoryImplementationLocal
import ru.myproject.mytrranslator.model.repository.RepositoryLocal
import ru.myproject.mytrranslator.room.HistoryDataBase
import ru.myproject.mytrranslator.view.history.HistoryInteractor
import ru.myproject.mytrranslator.view.history.HistoryViewModel
import ru.myproject.mytrranslator.view.main.MainInteractor
import ru.myproject.mytrranslator.view.main.MainViewModel

val application = module {
    // single указывает, что БД должна быть в единственном экземпляре
    single {
        Room.databaseBuilder(get(), HistoryDataBase::class.java, "HistoryDB").build()
    }

    // Получаем DAO
    single {
        get<HistoryDataBase>().historyDao()
    }

    single<Repository<List<DataModel>>> {
        RepositoryImplementation(RetrofitImplementation())
    }

    single<RepositoryLocal<List<DataModel>>> {
        RepositoryImplementationLocal(RoomDataBaseImplementation(get()))
    }
}

// Функция factory сообщает Koin, что эту зависимость нужно создавать каждый раз заново, что как раз подходит для Activity и её компонентов.
val mainScreen = module {
    factory { MainViewModel(get()) }
    factory { MainInteractor(get(), get()) }
}

val historyScreen = module {
    factory { HistoryViewModel(get()) }
    factory { HistoryInteractor(get(), get()) }
}

