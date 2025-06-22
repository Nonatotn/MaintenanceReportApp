

package com.example.maintenancereportapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.maintenancereportapp.data.AppDatabase
import com.example.maintenancereportapp.data.model.Report
import kotlinx.coroutines.launch

class ReportsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    val allReports: LiveData<List<Report>> = db.reportDao().getAllReports()

    fun addReport(report: Report) = viewModelScope.launch {
        db.reportDao().insert(report)
    }

    fun deleteReport(report: Report) = viewModelScope.launch {
        db.reportDao().delete(report)
    }
}
