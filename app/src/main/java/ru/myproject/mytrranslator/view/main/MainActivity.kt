package ru.myproject.mytrranslator.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
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
private const val REQUEST_CODE = 42

// Контракта уже нет
class MainActivity : BaseActivity<AppState, MainInteractor>() {

    // Создаём модель
    override lateinit var model: MainViewModel

    // Объявим переменную. Этот класс отвечает за загрузку динамического контента
    private lateinit var splitInstallManager: SplitInstallManager

    // Объявим переменную - она пригодится в дальнейшем
    private lateinit var appUpdateManager: AppUpdateManager

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

    // Слушатель обновления нужен только для гибких обновлений
    private val stateUpdatedListener: InstallStateUpdatedListener =
        InstallStateUpdatedListener { state ->
            // Переменная state позволяет следить за прогрессом установки
            state?.let {
                if (it.installStatus() == InstallStatus.DOWNLOADED) {
                    // Когда обновление скачалось и готово к установке, отображаем SnackBar
                    popupSnackbarForCompleteUpdate()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iniViewModel()
        initViews()
        checkForUpdates()
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                // Обновление скачано, но не установлено
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    // Обновление прервано - можно возобновить установку
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        IMMEDIATE,
                        this,
                        REQUEST_CODE
                    )
                }
            }
    }

    // Метод нужен в основном для обработки ошибок обновления
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Если всё в порядке, снимаем слушатель прогресса обновления
                appUpdateManager.unregisterListener(stateUpdatedListener)
            } else {
                // Если обновление прервано (пользователь не принял или прервал его) или не загружено (из-за проблем с соединением),
                // показываем уведомление (также можно показать диалоговое окно с предложением попробовать обновить еще раз)
                Toast.makeText(
                    applicationContext,
                    "Update flow failed! Result code: $resultCode",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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

    // Сам метод вызываем в onCreate
    private fun checkForUpdates() {
        // Создаём менеджер
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        // Возвращаем интент (appUpdateInfo), который мы будем использовать в качестве информации для обновления
        val appUpdateInfo = appUpdateManager.appUpdateInfo
        // Проверяем наличие обновления
        appUpdateInfo.addOnSuccessListener { appUpdateIntent ->
            if (appUpdateIntent.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateIntent.isUpdateTypeAllowed(IMMEDIATE)   // Здесь мы делаем проверку на немедленный тип обновления (IMMEDIATE);
                                                                    // для гибкого нужно передавать AppUpdateType.FLEXIBLE
            ) {
                // Передаём слушатель прогресса (только для гибкого типа обновления)
                appUpdateManager.registerListener(stateUpdatedListener)
                // Выполняем запрос
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateIntent,
                    IMMEDIATE,
                    this,
                    // Реквест-код для обработки запроса в onActivityResult
                    REQUEST_CODE
                )
            }
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            findViewById(R.id.activity_main_layout),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            show()
        }
    }
}