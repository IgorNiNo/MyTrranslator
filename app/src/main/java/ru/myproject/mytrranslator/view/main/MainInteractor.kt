package ru.myproject.mytrranslator.view.main

import io.reactivex.Observable
import ru.myproject.mytrranslator.model.data.AppState
import ru.myproject.mytrranslator.model.data.DataModel
import ru.myproject.mytrranslator.model.repository.Repository
import ru.myproject.mytrranslator.viewmodel.Interactor

class MainInteractor(

    // Снабжаем интерактор репозиторием для получения локальных или внешних данных
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: Repository<List<DataModel>>
) : Interactor<AppState> {

    // Интерактор лишь запрашивает у репозитория данные, детали имплементации интерактору неизвестны
    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> {
        return if (fromRemoteSource) {
            remoteRepository.getData(word).map { AppState.Success(it) }
        } else {
            localRepository.getData(word).map { AppState.Success(it) }
        }
    }
}