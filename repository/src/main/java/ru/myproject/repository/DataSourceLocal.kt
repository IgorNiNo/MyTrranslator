package ru.myproject.repository

import ru.myproject.model.data.AppState

// Наследуемся от DataSource и добавляем нужный метод
interface DataSourceLocal<T> : DataSource<T> {
    suspend fun saveToDB(appState: AppState)
}