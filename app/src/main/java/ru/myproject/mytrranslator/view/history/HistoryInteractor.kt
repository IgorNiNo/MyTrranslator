package ru.myproject.mytrranslator.view.history

import ru.myproject.mytrranslator.model.data.AppState
import ru.myproject.mytrranslator.model.data.DataModel
import ru.myproject.mytrranslator.model.repository.Repository
import ru.myproject.mytrranslator.model.repository.RepositoryLocal
import ru.myproject.mytrranslator.viewmodel.Interactor

// Класс мало чем отличается от интерактора, который мы уже описывали
class HistoryInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        return AppState.Success(
            if (fromRemoteSource) {
                repositoryRemote
            } else {
                repositoryLocal
            }.getData(word)
        )
    }
}