package ru.myproject.mytrranslator.view.history

import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_history.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.myproject.mytrranslator.R
import ru.myproject.mytrranslator.model.data.AppState
import ru.myproject.mytrranslator.model.data.DataModel
import ru.myproject.mytrranslator.view.base.BaseActivity

class HistoryActivity : BaseActivity<AppState, HistoryInteractor>() {

    override lateinit var model: HistoryViewModel
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        iniViewModel()
        initViews()
    }

    // Сразу запрашиваем данные из локального репозитория
    override fun onResume() {
        super.onResume()
        model.getData("", false)
    }

    // Вызовется из базовой Activity, когда данные будут готовы
    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }

    private fun iniViewModel() {
        if (history_activity_recyclerview.adapter != null) {
            throw IllegalStateException("The ViewModel should be initialised first")
        }
        val viewModel: HistoryViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(this@HistoryActivity, Observer<AppState> { renderData(it) })
    }

    // Инициализируем адаптер и передаем его в RecyclerView
    private fun initViews() {
        history_activity_recyclerview.adapter = adapter
    }
}