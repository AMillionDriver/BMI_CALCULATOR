package com.axoloth.bmicalculator.logic

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.OutputStream

/**
 * Membagikan hasil BMI melalui Intent (WhatsApp, Email, dll)
 */
fun shareBmiResult(context: Context, bmiDetails: String) {
    val message = """
        📊 *HASIL KALKULASI BMI* 📊
        
        $bmiDetails
        
        📅 Tanggal: ${java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date())}
        
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
    } catch (e: Exception) {
        Toast.makeText(context, "Tidak ada aplikasi untuk membagikan teks", Toast.LENGTH_SHORT).show()
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
        
        Dibuat pada: ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date())}
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
            // Fallback untuk Android 9 (Pie) ke bawah
            // Karena membutuhkan permission WRITE_EXTERNAL_STORAGE, kita sarankan user menggunakan Share saja
            // atau implementasi FileOutputStream manual (memerlukan pengecekan permission di UI)
            Toast.makeText(context, "Gunakan fitur 'Share' untuk versi Android ini", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Gagal mengunduh: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
