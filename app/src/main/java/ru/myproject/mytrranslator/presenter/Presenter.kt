package ru.myproject.mytrranslator.presenter

import ru.myproject.mytrranslator.model.data.AppState
import ru.myproject.mytrranslator.view.base.View

// На уровень выше View находится презентер, который уже ничего не знает ни о контексте, ни о фреймворке
interface Presenter<T : AppState, V : View> {

    fun attachView(view: V)

    fun detachView(view: V)

    // Получение данных с флагом isOnline(из Интернета или нет)
    fun getData(word: String, isOnline: Boolean)
}