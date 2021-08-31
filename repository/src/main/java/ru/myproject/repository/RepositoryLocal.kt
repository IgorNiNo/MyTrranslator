package ru.myproject.repository

import ru.myproject.model.data.AppState

// Наследуемся от Repository и добавляем нужный метод
interface RepositoryLocal<T> : Repository<T> {
    suspend fun saveToDB(appState: AppState)
}