package ru.myproject.mytrranslator.model.repository

import ru.myproject.mytrranslator.model.data.AppState

// Наследуемся от Repository и добавляем нужный метод
interface RepositoryLocal<T> : Repository<T> {
    suspend fun saveToDB(appState: AppState)
}