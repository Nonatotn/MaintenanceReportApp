package com.example.maintenancereportapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.provider.MediaStore
import com.example.maintenancereportapp.data.model.Report
import com.example.maintenancereportapp.data.model.ReportItem
import java.io.File
import java.io.FileOutputStream

object PdfUtil {
    fun createPdf(context: Context, report: Report, items: List<ReportItem>): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()  // A4
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        val paint = Paint()
        paint.textSize = 12f

        // Cabeçalho
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Relatório de Manutenção", 40f, 40f, paint)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Título: ${report.title}", 40f, 60f, paint)
        canvas.drawText("Cliente: ${report.client}", 40f, 80f, paint)
        canvas.drawText("Responsável: ${report.responsible}", 40f, 100f, paint)

        var yPosition = 130f
        var pageNumber = 1

        for (item in items) {
            if (yPosition > 800f) {
                // finalizar página e criar nova
                pdfDocument.finishPage(page)
                pageNumber++
                page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNumber).create())
                canvas = page.canvas
                yPosition = 40f
            }
            if (item.itemType == 0) {
                // Texto
                canvas.drawText("- ${item.text}", 50f, yPosition, paint)
                yPosition += 20f
            } else if (item.itemType == 1) {
                // Imagem
                try {
                    val uri = Uri.parse(item.imageUri)
                    val bmp: Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    // Ajustar tamanho proporcional
                    val maxWidth = 400
                    val ratio = bmp.height.toFloat() / bmp.width
                    val height = (maxWidth * ratio).toInt()
                    val scaled = Bitmap.createScaledBitmap(bmp, maxWidth, height, true)
                    if (yPosition + scaled.height + 20f > 842f) {
                        // nova página
                        pdfDocument.finishPage(page)
                        pageNumber++
                        page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNumber).create())
                        canvas = page.canvas
                        yPosition = 40f
                    }
                    canvas.drawBitmap(scaled, 50f, yPosition, paint)
                    yPosition += scaled.height + 20f
                    canvas.drawText("Legenda: ${item.caption}", 50f, yPosition, paint)
                    yPosition += 30f
                } catch (e: Exception) {
                    // Se falhar ao carregar imagem, ignorar ou desenhar aviso
                    canvas.drawText("(Imagem não disponível)", 50f, yPosition, paint)
                    yPosition += 20f
                }
            }
        }
        pdfDocument.finishPage(page)

        // Salvar arquivo no diretório externo da app
        val file = File(context.getExternalFilesDir(null), "report_${report.id}.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()
        return file
    }
}
