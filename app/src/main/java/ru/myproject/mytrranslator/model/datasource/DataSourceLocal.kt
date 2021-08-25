package ru.myproject.mytrranslator.model.datasource

import ru.myproject.mytrranslator.model.data.AppState

// Наследуемся от DataSource и добавляем нужный метод
interface DataSourceLocal<T> : DataSource<T> {
    suspend fun saveToDB(appState: AppState)
}