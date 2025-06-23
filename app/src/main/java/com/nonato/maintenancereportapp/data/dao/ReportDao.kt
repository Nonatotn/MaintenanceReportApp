import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nonato.maintenancereportapp.data.model.Report

@Dao
interface ReportDao {

    @Insert
    suspend fun insert(report: Report): Long

    @Update
    suspend fun update(report: Report)

    @Delete
    suspend fun delete(report: Report)

    @Query("SELECT * FROM reports ORDER BY id DESC")
    fun getAllReports(): LiveData<List<Report>>

    // Opção 1: Renomear as funções
    @Query("SELECT * FROM reports WHERE id = :id")
    fun getReportByIdSync(id: Long): Report // Renomeada

    @Query("SELECT * FROM reports WHERE id = :reportId")
    fun getReportByIdLiveData(reportId: Long): LiveData<Report> // Renomeada
}