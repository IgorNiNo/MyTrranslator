package ru.myproject.historyscreen.view

import ru.myproject.core.viewmodel.Interactor
import ru.myproject.model.data.AppState
import ru.myproject.model.data.DataModel
import ru.myproject.repository.Repository
import ru.myproject.repository.RepositoryLocal

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