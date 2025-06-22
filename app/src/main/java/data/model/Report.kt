package data.model

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val client: String,
    val responsible: String
)
