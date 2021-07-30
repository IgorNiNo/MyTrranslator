package ru.myproject.mytrranslator.model.repository

import io.reactivex.Observable
import ru.myproject.mytrranslator.model.datasource.DataSource
import ru.myproject.mytrranslator.model.data.DataModel

class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) : Repository<List<DataModel>> {

    // Репозиторий возвращает данные, используя dataSource (локальный или внешний)
    override fun getData(word: String): Observable<List<DataModel>> {
        return dataSource.getData(word)
    }
}