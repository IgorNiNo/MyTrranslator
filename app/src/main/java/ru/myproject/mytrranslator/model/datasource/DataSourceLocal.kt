package ru.myproject.mytrranslator.model.datasource

import io.reactivex.Observable
import ru.myproject.mytrranslator.model.data.DataModel

// Для локальных данных используется Room
class DataSourceLocal(
    private val remoteProvider: RoomDataBaseImplementation
    = RoomDataBaseImplementation()
) : DataSource<List<DataModel>> {
    override fun getData(word: String): Observable<List<DataModel>> = remoteProvider.getData(word)
}