package com.axoloth.bmicalculator.logic

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.graphics.toColorInt
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Membagikan hasil BMI melalui Intent (WhatsApp, Email, dll)
 */
fun shareBmiResult(context: Context, bmiDetails: String) {
    val message = """
        📊 *HASIL KALKULASI BMI* 📊
        
        $bmiDetails
        
        📅 Tanggal: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())}
        
        Pantau kesehatanmu setiap hari dengan aplikasi BMI Calculator! 💪
    """.trimIndent()

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "Bagikan hasil via:")
    try {
        context.startActivity(shareIntent)
    } catch (ignored: Exception) {
        Toast.makeText(context, "Tidak ada aplikasi untuk membagikan teks", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Menyimpan hasil BMI sebagai PDF dengan template laporan medis yang kaya informasi
 */
fun exportAsPdf(context: Context, weight: String, height: String, bmi: String, category: String) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Ukuran A4
    val page = pdfDocument.startPage(pageInfo)
    val canvas: Canvas = page.canvas
    val paint = Paint()

    val weightVal = weight.toDoubleOrNull() ?: 0.0
    val heightVal = height.toDoubleOrNull() ?: 0.0
    val bmiVal = bmi.toDoubleOrNull() ?: 0.0
    
    // Hitung berat ideal (Standard WHO: BMI 18.5 - 24.9)
    val idealMin = 18.5 * (heightVal / 100) * (heightVal / 100)
    val idealMax = 24.9 * (heightVal / 100) * (heightVal / 100)
    val idealRange = "${String.format("%.1f", idealMin)} kg - ${String.format("%.1f", idealMax)} kg"

    // Saran berdasarkan kategori
    val saran = when {
        bmiVal < 18.5 -> "Saran: Tingkatkan asupan kalori bernutrisi dan konsultasikan program penambahan berat badan."
        bmiVal < 25.0 -> "Saran: Pertahankan pola makan sehat dan olahraga teratur untuk menjaga berat badan ideal."
        bmiVal < 30.0 -> "Saran: Kurangi konsumsi gula/lemak berlebih dan tingkatkan aktivitas fisik harian."
        else -> "Saran: Segera konsultasikan dengan ahli gizi, kurangi junk food, dan mulai program olahraga rutin."
    }

    // 1. Header
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = 24f
    paint.color = Color.BLACK
    canvas.drawText("LAPORAN KESEHATAN BMI", 160f, 60f, paint)
    
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 12f
    canvas.drawText("Dibuat pada: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}", 160f, 80f, paint)
    
    // Garis Pemisah
    paint.strokeWidth = 2f
    canvas.drawLine(40f, 100f, 555f, 100f, paint)

    // 2. Data Pengukuran
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = 16f
    canvas.drawText("I. DETAIL PENGUKURAN", 40f, 140f, paint)

    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 14f
    canvas.drawText("Tinggi Badan   : $height cm", 50f, 170f, paint)
    canvas.drawText("Berat Badan    : $weight kg", 50f, 195f, paint)
    
    // 3. Analisis BMI
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText("II. ANALISIS KESEHATAN", 40f, 240f, paint)
    
    paint.color = "#3B82F6".toColorInt() // Biru
    paint.textSize = 20f
    canvas.drawText("SKOR BMI: $bmi", 50f, 275f, paint)
    
    paint.color = if (category == "Normal") "#4CAF50".toColorInt() else Color.RED
    canvas.drawText("STATUS  : $category", 50f, 305f, paint)

    // 4. Berat Ideal & Saran
    paint.color = Color.BLACK
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = 16f
    canvas.drawText("III. REKOMENDASI & SARAN", 40f, 360f, paint)

    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 14f
    canvas.drawText("Berat Badan Ideal Anda : $idealRange", 50f, 390f, paint)
    
    // Saran (Multi-line handling sederhana)
    paint.textSize = 13f
    if (saran.length > 60) {
        val part1 = saran.substring(0, saran.lastIndexOf(" ", 60))
        val part2 = saran.substring(saran.lastIndexOf(" ", 60)).trim()
        canvas.drawText(part1, 50f, 420f, paint)
        canvas.drawText(part2, 50f, 440f, paint)
    } else {
        canvas.drawText(saran, 50f, 420f, paint)
    }

    // 5. Tabel Referensi WHO
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = 14f
    canvas.drawText("IV. STANDAR REFERENSI (WHO)", 40f, 500f, paint)
    
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 12f
    val startY = 530f
    canvas.drawText("- < 18.5       : Berat Badan Kurang (Underweight)", 60f, startY, paint)
    canvas.drawText("- 18.5 - 24.9  : Normal (Healthy Weight)", 60f, startY + 20, paint)
    canvas.drawText("- 25.0 - 29.9  : Berat Badan Berlebih (Overweight)", 60f, startY + 40, paint)
    canvas.drawText("- > 30.0       : Obesitas (Obese)", 60f, startY + 60, paint)

    // 6. Disclaimer (Sanggahan Penting)
    paint.color = Color.RED
    paint.textSize = 10f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText("PENTING:", 40f, 700f, paint)
    
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
    paint.color = Color.DKGRAY
    canvas.drawText("Laporan ini hanya bersifat referensi/perkiraan. Harap konsultasikan kesehatan Anda", 40f, 720f, paint)
    canvas.drawText("dengan dokter atau tenaga medis profesional. Jangan pernah menjadikan aplikasi ini", 40f, 735f, paint)
    canvas.drawText("sebagai satu-satunya patokan kesehatan Anda.", 40f, 750f, paint)

    // Footer
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 10f
    paint.color = Color.GRAY
    canvas.drawText("Aplikasi BMI Calculator - Referensi Medis Mandiri", 180f, 800f, paint)

    pdfDocument.finishPage(page)

    // Simpan ke Folder Downloads
    val fileName = "BMI_Medical_Report_${System.currentTimeMillis()}.pdf"
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
                Toast.makeText(context, "PDF Berhasil diunduh ke folder Downloads", Toast.LENGTH_LONG).show()
            }
        }
        pdfDocument.close()
    } catch (e: Exception) {
        Toast.makeText(context, "Gagal simpan PDF: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Menyimpan hasil BMI ke dalam folder Downloads sebagai file .txt
 */
fun downloadBmiAsTxt(context: Context, bmiDetails: String) {
    val fileName = "BMI_Report_${System.currentTimeMillis()}.txt"
    val content = """
        LAPORAN HASIL BMI
        =================
        
        $bmiDetails
        
        Dibuat pada: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}
        Aplikasi: BMI Calculator
    """.trimIndent()

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(content.toByteArray())
                }
                Toast.makeText(context, "File berhasil disimpan di folder 'Downloads'", Toast.LENGTH_LONG).show()
            } ?: throw Exception("Gagal membuat file di sistem")
        } else {
            Toast.makeText(context, "Gunakan fitur 'Share' untuk versi Android ini", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Gagal mengunduh: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
