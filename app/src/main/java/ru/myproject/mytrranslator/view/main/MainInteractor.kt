package ru.myproject.mytrranslator.view.main

import ru.myproject.mytrranslator.model.data.AppState
import ru.myproject.mytrranslator.model.data.DataModel
import ru.myproject.mytrranslator.model.repository.Repository
import ru.myproject.mytrranslator.model.repository.RepositoryLocal
import ru.myproject.mytrranslator.viewmodel.Interactor

class MainInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        val appState: AppState
        // Теперь полученное слово мы сохраняем в БД.
        // Сделать это нужно именно здесь, в соответствии с принципами чистой архитектуры: интерактор обращается к репозиторию
        if (fromRemoteSource) {
            appState = AppState.Success(repositoryRemote.getData(word))
            repositoryLocal.saveToDB(appState)
        } else {
            appState = AppState.Success(repositoryLocal.getData(word))
        }
        return appState
    }
}