package ru.myproject.mytrranslator.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import kotlinx.android.synthetic.main.activity_main.*
import ru.myproject.mytrranslator.R
import ru.myproject.utils.network.isOnline
import ru.myproject.mytrranslator.view.main.adapter.MainAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import ru.myproject.core.BaseActivity
import ru.myproject.model.data.AppState
import ru.myproject.model.data.DataModel
import ru.myproject.mytrranslator.convertMeaningsToString
import ru.myproject.mytrranslator.di.injectDependencies
import ru.myproject.mytrranslator.view.descriptionscreen.DescriptionActivity

private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG = "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
private const val HISTORY_ACTIVITY_PATH = "ru.myproject.historyscreen.view.HistoryActivity"
private const val HISTORY_ACTIVITY_FEATURE_NAME = "historyScreen"

// Контракта уже нет
class MainActivity : BaseActivity<AppState, MainInteractor>() {

    // Создаём модель
    override lateinit var model: MainViewModel

    // Объявим переменную. Этот класс отвечает за загрузку динамического контента
    private lateinit var splitInstallManager: SplitInstallManager

    // Передаём в адаптер слушатель нажатия на список
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
                startActivity(
                    DescriptionActivity.getIntent(
                        this@MainActivity,
                        data.text!!,
                        convertMeaningsToString(data.meanings!!),
                        data.meanings!![0].imageUrl
                    )
                )
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iniViewModel()
        initViews()
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                // Создаём менеджер
                splitInstallManager = SplitInstallManagerFactory.create(applicationContext)
                // Создаём запрос на создание экрана
                val request =
                    SplitInstallRequest
                        .newBuilder()
                        .addModule(HISTORY_ACTIVITY_FEATURE_NAME)
                        .build()

                splitInstallManager
                    .startInstall(request)
                    // Добавляем слушатель в случае успеха
                    .addOnSuccessListener {
                        // Открываем экран
                        val intent = Intent().setClassName(packageName, HISTORY_ACTIVITY_PATH)
                        startActivity(intent)
                    }
                    // Добавляем слушатель в случае, если что-то пошло не так
                    .addOnFailureListener {
                        // Обрабатываем ошибку
                        Toast.makeText(
                            applicationContext,
                            "Couldn't download feature: " + it.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun iniViewModel() {
        check(main_activity_recyclerview.adapter == null) { "The ViewModel should be initialised first" }
        injectDependencies()
        // Теперь ViewModel инициализируется через функцию by viewModel(). Это функция, предоставляемая Koin из коробки
        val viewModel: MainViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(this@MainActivity, Observer<AppState> {
            renderData(it)
        })
    }

    private fun initViews() {
        search_fab.setOnClickListener(fabClickListener)
        main_activity_recyclerview.adapter = adapter
    }

//    companion object {
//        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
//            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
//    }
}