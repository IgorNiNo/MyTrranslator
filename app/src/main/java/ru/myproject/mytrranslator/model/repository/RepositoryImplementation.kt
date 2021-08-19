package ru.myproject.mytrranslator.model.repository

import ru.myproject.mytrranslator.model.datasource.DataSource
import ru.myproject.mytrranslator.model.data.DataModel

class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) : Repository<List<DataModel>> {

    // Репозиторий возвращает данные, используя dataSource (локальный или внешний)
    // Добавляем suspend
    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }
}