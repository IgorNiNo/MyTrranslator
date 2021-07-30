package ru.myproject.mytrranslator.model.datasource

import io.reactivex.Observable
import ru.myproject.mytrranslator.model.data.DataModel

class RoomDataBaseImplementation : DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> {
        TODO("not implemented") // Для изменения тела созданных функций используйте Файл | Настройки | Шаблоны файлов
    }
}