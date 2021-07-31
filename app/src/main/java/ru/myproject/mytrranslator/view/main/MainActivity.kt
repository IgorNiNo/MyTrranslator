package ru.myproject.mytrranslator.view.main

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.myproject.mytrranslator.R
import ru.myproject.mytrranslator.model.data.AppState
import ru.myproject.mytrranslator.model.data.DataModel
import ru.myproject.mytrranslator.view.base.BaseActivity
import ru.myproject.mytrranslator.view.main.adapter.MainAdapter

// Контракта уже нет
class MainActivity : BaseActivity<AppState, MainInteractor>() {

    // Создаём модель
    override val model: MainViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(MainViewModel::class.java)
    }

    // Паттерн Observer в действии. Именно с его помощью мы подписываемся на изменения в LiveData
    private val observer = Observer<AppState> { renderData(it) }

    // Адаптер для отображения списка вариантов перевода
    private var adapter: MainAdapter? = null

    // Обработка нажатия элемента списка
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(this@MainActivity, data.text, Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        search_fab.setOnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(object : SearchDialogFragment.OnSearchClickListener {
                override fun onClick(searchWord: String) {
                    // Обратите внимание на этот ключевой момент. У ViewModel мы получаем LiveData
                    // через метод getData и подписываемся на изменения, передавая туда observer
                    model.getData(searchWord, true).observe(this@MainActivity, observer)
                }
            })
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }

    override fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val dataModel = appState.data
                if (dataModel == null || dataModel.isEmpty()) {
                    showErrorScreen(getString(R.string.empty_server_response_on_success))
                } else {
                    showViewSuccess()
                    if (adapter == null) {
                        main_activity_recyclerview.layoutManager =
                            LinearLayoutManager(applicationContext)
                        main_activity_recyclerview.adapter =
                            MainAdapter(onListItemClickListener, dataModel)
                    } else {
                        adapter!!.setData(dataModel)
                    }
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
                showErrorScreen(appState.error.message)
            }
        }
    }

    private fun showErrorScreen(error: String?) {
        showViewError()
        error_textview.text = error ?: getString(R.string.undefined_error)
        reload_button.setOnClickListener {
            // В случае ошибки мы повторно запрашиваем данные и подписываемся на изменения
            model.getData("hi", true).observe(this, observer)
        }
    }

    private fun showViewSuccess() {
        success_linear_layout.visibility = VISIBLE
        loading_frame_layout.visibility = GONE
        error_linear_layout.visibility = GONE
    }

    private fun showViewLoading() {
        success_linear_layout.visibility = GONE
        loading_frame_layout.visibility = VISIBLE
        error_linear_layout.visibility = GONE
    }

    private fun showViewError() {
        success_linear_layout.visibility = GONE
        loading_frame_layout.visibility = GONE
        error_linear_layout.visibility = VISIBLE
    }

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG = "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }
}