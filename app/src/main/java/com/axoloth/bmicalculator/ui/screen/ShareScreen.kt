package com.axoloth.bmicalculator.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.axoloth.bmicalculator.logic.downloadBmiAsTxt
import com.axoloth.bmicalculator.logic.exportAsPdf
import com.axoloth.bmicalculator.logic.shareBmiResult
import com.axoloth.bmicalculator.ui.theme.BMICALCULATORTheme

data class ShareOption(val title: String, val icon: ImageVector, val actionType: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareScreen(onBackClick: () -> Unit = {}) {
    val context = LocalContext.current
    
    // Data dummy untuk konten sharing (nanti bisa diambil dari database)
    val weight = "70"
    val height = "170"
    val bmiValue = "24.22"
    val category = "Normal"
    val dummyBmiInfo = "BMI: $bmiValue\nKategori: $category\nBerat: ${weight}kg / Tinggi: ${height}cm"
    
    val shareOptions = listOf(
        ShareOption("Bagikan Teks (WhatsApp/Lainnya)", Icons.Default.Share, "TEXT"),
        ShareOption("Simpan sebagai PDF (Laporan Medis)", Icons.Default.PictureAsPdf, "PDF"),
        ShareOption("Download Laporan (.txt)", Icons.Default.Download, "DOWNLOAD"),
        ShareOption("Export Excel (.xlsx)", Icons.Default.TableChart, "EXCEL")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Share / Export") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(shareOptions) { option ->
                ListItem(
                    headlineContent = { Text(option.title) },
                    leadingContent = { 
                        Icon(
                            imageVector = option.icon, 
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        ) 
                    },
                    modifier = Modifier.clickable {
                        when (option.actionType) {
                            "TEXT" -> shareBmiResult(context, dummyBmiInfo)
                            "DOWNLOAD" -> downloadBmiAsTxt(context, dummyBmiInfo)
                            "PDF" -> exportAsPdf(context, weight, height, bmiValue, category)
                            else -> {
                                // Logic Excel akan menyusul
                            }
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShareScreenPreview() {
    BMICALCULATORTheme {
        ShareScreen()
    }
}
