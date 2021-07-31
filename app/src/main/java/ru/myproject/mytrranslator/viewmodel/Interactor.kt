package ru.myproject.mytrranslator.viewmodel

import io.reactivex.Observable

// Интерактор перенесем в слой ViewModel. Здесь уже чистая бизнес-логика
interface Interactor<T> {

    // Use Сase: получение данных для вывода на экран
    // Используем RxJava
    fun getData(word: String, fromRemoteSource: Boolean): Observable<T>
}