package ru.myproject.mytrranslator.model.datasource

import io.reactivex.Observable
import ru.myproject.mytrranslator.model.data.DataModel

// Для получения внешних данных мы будем использовать Retrofit
class DataSourceRemote(
    private val remoteProvider: RetrofitImplementation
    = RetrofitImplementation()
) : DataSource<List<DataModel>> {
    override fun getData(word: String): Observable<List<DataModel>> = remoteProvider.getData(word)
}
