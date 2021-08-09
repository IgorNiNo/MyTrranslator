package ru.myproject.mytrranslator.model.repository

// Репозиторий представляет собой слой получения и хранения данных, которые он передаёт интерактору
interface Repository<T> {
    // Добавляем suspend
    suspend fun getData(word: String): T
}