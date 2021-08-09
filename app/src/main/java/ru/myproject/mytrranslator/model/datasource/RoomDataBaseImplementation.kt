package ru.myproject.mytrranslator.model.datasource

import ru.myproject.mytrranslator.model.data.DataModel

class RoomDataBaseImplementation : DataSource<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        TODO("not implemented") // Для изменения тела созданных функций используйте Файл | Настройки | Шаблоны файлов
    }
}