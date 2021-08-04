package ru.myproject.mytrranslator.view.main

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import ru.myproject.mytrranslator.R
import ru.myproject.mytrranslator.model.data.AppState
import ru.myproject.mytrranslator.model.data.DataModel
import ru.myproject.mytrranslator.utils.network.isOnline
import ru.myproject.mytrranslator.view.base.BaseActivity
import ru.myproject.mytrranslator.view.main.adapter.MainAdapter
import javax.inject.Inject

// Контракта уже нет
class MainActivity : BaseActivity<AppState, MainInteractor>() {

    // Внедряем фабрику для создания ViewModel
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    // Создаём модель
    override lateinit var model: MainViewModel

    // Адаптер для отображения списка вариантов перевода
    private val adapter: MainAdapter by lazy {
        MainAdapter(onListItemClickListener)
    }

    private val fabClickListener: View.OnClickListener =
        View.OnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(onSearchClickListener)
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }

    // Обработка нажатия элемента списка
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(this@MainActivity, data.text, Toast.LENGTH_SHORT).show()
            }
        }

    private val onSearchClickListener: SearchDialogFragment.OnSearchClickListener =
        object : SearchDialogFragment.OnSearchClickListener {
            override fun onClick(searchWord: String) {
                isNetworkAvailable = isOnline(applicationContext)
                if (isNetworkAvailable) {
                    model.getData(searchWord, isNetworkAvailable)
                } else {
                    showNoInternetConnectionDialog()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {

        // Сообщаем Dagger’у, что тут понадобятся зависимости
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Фабрика уже готова, можно создавать ViewModel
        model = viewModelFactory.create(MainViewModel::class.java)
        model.subscribe().observe(this@MainActivity, Observer<AppState> {
            renderData(it)
        })

        search_fab.setOnClickListener(fabClickListener)
        main_activity_recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        main_activity_recyclerview.adapter = adapter
    }

    override fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                showViewWorking()
                val data = appState.data
                if (data.isNullOrEmpty()) {
                    showAlertDialog(
                        getString(R.string.dialog_tittle_sorry),
                        getString(R.string.empty_server_response_on_success)
                    )
                } else {
                    adapter.setData(data)
                }
            }
            is AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    progress_bar_horizontal.visibility = VISIBLE
                    progress_bar_round.visibility = GONE
                    progress_bar_horizontal.progress = appState.progress
                } else {
                    progress_bar_horizontal.visibility = GONE
                    progress_bar_round.visibility = VISIBLE
                }
            }
            is AppState.Error -> {
                showViewWorking()
                showAlertDialog(getString(R.string.error_stub), appState.error.message)
            }
        }
    }

    private fun showViewWorking() {
        loading_frame_layout.visibility = GONE
    }

    private fun showViewLoading() {
        loading_frame_layout.visibility = VISIBLE
    }

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }
}