package com.nonato.maintenancereportapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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