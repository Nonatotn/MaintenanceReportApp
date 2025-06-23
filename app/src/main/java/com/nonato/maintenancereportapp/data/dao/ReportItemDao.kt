package com.nonato.maintenancereportapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nonato.maintenancereportapp.data.model.ReportItem

@Dao
interface ReportItemDao {
    @Insert
    suspend fun insert(item: ReportItem)
    @Update
    suspend fun update(item: ReportItem)
    @Delete
    suspend fun delete(item: ReportItem)

    @Query("SELECT * FROM items WHERE reportId = :reportId ORDER BY id")
    fun getItemsForReport(reportId: Long): LiveData<List<ReportItem>>
}
