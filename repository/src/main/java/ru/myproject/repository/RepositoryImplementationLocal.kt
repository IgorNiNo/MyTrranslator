package ru.myproject.repository

import ru.myproject.model.data.AppState
import ru.myproject.model.data.DataModel

// RepositoryImplementationLocal теперь содержит два метода, наследуется от RepositoryLocal и в конструктор получает инстанс DataSourceLocal
class RepositoryImplementationLocal(private val dataSource: DataSourceLocal<List<DataModel>>) : RepositoryLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }

    override suspend fun saveToDB(appState: AppState) {
        dataSource.saveToDB(appState)
    }
}