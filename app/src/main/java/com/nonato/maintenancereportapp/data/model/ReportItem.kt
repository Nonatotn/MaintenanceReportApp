package com.nonato.maintenancereportapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = Report::class,
            parentColumns = ["id"],
            childColumns = ["reportId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("reportId")]
)
data class ReportItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reportId: Long,
    val itemType: Int,      // 0 = texto, 1 = imagem
    val text: String?,      // usado se itemType = 0
    val imageUri: String?,  // usado se itemType = 1 (URI da imagem salva)
    val caption: String?    // legenda da imagem (ou opcional para texto)
)
