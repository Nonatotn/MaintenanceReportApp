package data

@Database(entities = [Report::class, ReportItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
    abstract fun reportItemDao(): ReportItemDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "maintenance_reports.db"
                ).build().also { INSTANCE = it }
            }
    }
}
