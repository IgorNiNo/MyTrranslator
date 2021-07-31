package ru.myproject.mytrranslator.view.base

import androidx.appcompat.app.AppCompatActivity
import ru.myproject.mytrranslator.model.data.AppState
import ru.myproject.mytrranslator.viewmodel.BaseViewModel
import ru.myproject.mytrranslator.viewmodel.Interactor

abstract class BaseActivity<T : AppState, I:Interactor<T>> : AppCompatActivity() {

    abstract val model: BaseViewModel<T>

    abstract fun renderData(dataModel: T)
}