package data.dao

@Dao
interface ReportDao {
    @Insert suspend fun insert(report: Report): Long
    @Update suspend fun update(report: Report)
    @Delete suspend fun delete(report: Report)

    @Query("SELECT * FROM reports ORDER BY id DESC")
    fun getAllReports(): LiveData<List<Report>>
}
