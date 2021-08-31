package ru.myproject.mytrranslator.di

import androidx.room.Room
import org.koin.dsl.module
import ru.myproject.historyscreen.view.HistoryInteractor
import ru.myproject.historyscreen.view.HistoryViewModel
import ru.myproject.model.data.DataModel
import ru.myproject.repository.RetrofitImplementation
import ru.myproject.repository.RoomDataBaseImplementation
import ru.myproject.repository.Repository
import ru.myproject.repository.RepositoryImplementation
import ru.myproject.repository.RepositoryImplementationLocal
import ru.myproject.repository.RepositoryLocal
import ru.myproject.repository.room.HistoryDataBase
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

