package ru.myproject.core.viewmodel

// Интерактор перенесем в слой ViewModel. Здесь уже чистая бизнес-логика
interface Interactor<T> {

    // Добавляем suspend
    suspend fun getData(word: String, fromRemoteSource: Boolean): T
}