package ru.myproject.mytrranslator.model.repository

import ru.myproject.mytrranslator.model.data.AppState
import ru.myproject.mytrranslator.model.data.DataModel
import ru.myproject.mytrranslator.model.datasource.DataSourceLocal

// RepositoryImplementationLocal теперь содержит два метода, наследуется от RepositoryLocal и в конструктор получает инстанс DataSourceLocal
class RepositoryImplementationLocal(private val dataSource: DataSourceLocal<List<DataModel>>) : RepositoryLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }

    override suspend fun saveToDB(appState: AppState) {
        dataSource.saveToDB(appState)
    }
}