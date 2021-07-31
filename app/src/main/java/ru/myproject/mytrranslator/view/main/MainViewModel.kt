package ru.myproject.mytrranslator.view.main

import androidx.lifecycle.LiveData
import io.reactivex.observers.DisposableObserver
import ru.myproject.mytrranslator.model.data.AppState
import ru.myproject.mytrranslator.model.datasource.DataSourceLocal
import ru.myproject.mytrranslator.model.datasource.DataSourceRemote
import ru.myproject.mytrranslator.model.repository.RepositoryImplementation
import ru.myproject.mytrranslator.viewmodel.BaseViewModel

class MainViewModel(
    private val interactor: MainInteractor = MainInteractor(
        RepositoryImplementation(DataSourceRemote()),
        RepositoryImplementation(DataSourceLocal())
    )
) : BaseViewModel<AppState>() {

    // В этой переменной хранится последнее состояние Activity
    private var appState: AppState? = null

    // Переопределяем метод из BaseViewModel
    override fun getData(word: String, isOnline: Boolean): LiveData<AppState> {
        compositeDisposable.add(
            interactor.getData(word, isOnline)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
        return super.getData(word, isOnline)
    }

    private fun getObserver(): DisposableObserver<AppState> {
        return object : DisposableObserver<AppState>() {

            // Данные успешно загружены; сохраняем их и передаем во View (через LiveData). View сама разберётся, как их отображать
            override fun onNext(state: AppState) {
                appState = state
                liveDataForViewToObserve.value = state
            }

            // В случае ошибки передаём её в Activity таким же образом через LiveData
            override fun onError(e: Throwable) {
                liveDataForViewToObserve.value = AppState.Error(e)
            }

            override fun onComplete() {
            }
        }
    }
}