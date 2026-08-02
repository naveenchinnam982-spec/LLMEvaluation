package project.llmevaluation.utils

import android.content.Context
import android.widget.Toast
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import project.llmevaluation.models.EvaluationResult
import java.io.File
import java.io.FileOutputStream

object ExportManager {

    fun exportToPdf(context: Context, results: List<EvaluationResult>, filename: String) {
        try {
            val path = context.getExternalFilesDir(null)?.absolutePath + "/$filename.pdf"
            val file = File(path)
            val writer = PdfWriter(FileOutputStream(file))
            val pdf = PdfDocument(writer)
            val document = Document(pdf)

            document.add(Paragraph("LLM Evaluation Study Report").setBold().setFontSize(20f))
            document.add(Paragraph("Generated on: ${System.currentTimeMillis()}"))

            val table = Table(floatArrayOf(3f, 1f, 1f, 1f))
            table.addCell("Model")
            table.addCell("BLEU")
            table.addCell("ROUGE")
            table.addCell("Overall Score")

            results.forEach { res ->
                table.addCell(res.model)
                table.addCell("%.2f".format(res.bleu))
                table.addCell("%.2f".format(res.rouge))
                table.addCell("%.2f".format(res.overallScore))
            }

            document.add(table)
            document.close()
            Toast.makeText(context, "PDF Exported to: $path", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun exportToCsv(context: Context, results: List<EvaluationResult>, filename: String) {
        try {
            val path = context.getExternalFilesDir(null)?.absolutePath + "/$filename.csv"
            val file = File(path)
            val out = FileOutputStream(file)
            
            val header = "Model,Accuracy,Coherence,BLEU,ROUGE,Overall\n"
            out.write(header.toByteArray())
            
            results.forEach { res ->
                val line = "${res.model},${res.accuracy},${res.coherence},${res.bleu},${res.rouge},${res.overallScore}\n"
                out.write(line.toByteArray())
            }
            
            out.close()
            Toast.makeText(context, "CSV Exported to: $path", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
