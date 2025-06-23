package com.nonato.maintenancereportapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.nonato.maintenancereportapp.data.AppDatabase
import com.nonato.maintenancereportapp.data.model.Report
import com.nonato.maintenancereportapp.data.model.ReportItem
import kotlinx.coroutines.launch

class ReportDetailViewModel(
    application: Application,
    private val reportIdParam: Long
) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)

    // Usa o método DAO para obter o relatório pelo ID como LiveData
    val report: LiveData<Report> = db.reportDao().getReportById(reportIdParam)

    // Continua obtendo itens do relatório normalmente
    val items: LiveData<List<ReportItem>> = db.reportItemDao().getItemsForReport(reportIdParam)

    fun addItem(item: ReportItem) = viewModelScope.launch {
        db.reportItemDao().insert(item)
    }

    fun deleteItem(item: ReportItem) = viewModelScope.launch {
        db.reportItemDao().delete(item)
    }
}


