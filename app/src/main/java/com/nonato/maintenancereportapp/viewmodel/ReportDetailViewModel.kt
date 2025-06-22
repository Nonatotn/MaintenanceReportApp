package com.nonato.maintenancereportapp.viewmodel

package com.example.maintenancereportapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.maintenancereportapp.data.AppDatabase
import com.example.maintenancereportapp.data.model.Report
import com.example.maintenancereportapp.data.model.ReportItem
import kotlinx.coroutines.launch

class ReportDetailViewModel(application: Application, private val reportIdParam: Long)
    : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val _report = MutableLiveData<Report>()
    val report: LiveData<Report> get() = _report

    val items: LiveData<List<ReportItem>> = db.reportItemDao().getItemsForReport(reportIdParam)

    init {
        if (reportIdParam != 0L) {
            viewModelScope.launch {
                val list = db.reportDao().getAllReports().value // não ideal, mas para exemplo
                // Melhor: criar método DAO para buscar relatório por ID
                // Supondo que implementou: db.reportDao().getReportById(reportIdParam)
                // Aqui, se não tiver DAO para buscar por ID, omita carregar título/cliente inicialmente.
            }
        }
    }

    fun addItem(item: ReportItem) = viewModelScope.launch {
        db.reportItemDao().insert(item)
    }

    fun deleteItem(item: ReportItem) = viewModelScope.launch {
        db.reportItemDao().delete(item)
    }
}

class ReportDetailViewModelFactory(
    private val application: Application,
    private val reportId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReportDetailViewModel(application, reportId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
