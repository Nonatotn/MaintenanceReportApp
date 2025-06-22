package data.dao

@Dao
interface ReportItemDao {
    @Insert suspend fun insert(item: ReportItem)
    @Update suspend fun update(item: ReportItem)
    @Delete suspend fun delete(item: ReportItem)

    @Query("SELECT * FROM items WHERE reportId = :reportId ORDER BY id")
    fun getItemsForReport(reportId: Long): LiveData<List<ReportItem>>
}
