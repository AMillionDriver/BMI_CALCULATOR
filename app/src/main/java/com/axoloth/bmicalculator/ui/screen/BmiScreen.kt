package com.axoloth.bmicalculator.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.axoloth.bmicalculator.logic.BmiDarkModeToggle
import com.axoloth.bmicalculator.logic.calculateBmi
import com.axoloth.bmicalculator.logic.getBmiCategory
import com.axoloth.bmicalculator.ui.theme.BMICALCULATORTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BmiCallculator(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {}
) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var waist by remember { mutableStateOf("") }
    var bmiResult by remember { mutableDoubleStateOf(0.0) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "BMI App Menu",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider()
                
                Box(modifier = Modifier.padding(16.dp)) {
                    BmiDarkModeToggle(
                        isDarkMode = isDarkMode,
                        onToggle = onDarkModeChange
                    )
                }
                
                NavigationDrawerItem(
                    label = { Text("Tutup Menu") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Login") },
                    selected = false,
                    onClick = { /* Handle login */ }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Kalkulator BMI") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                ExportFAB {
                    // Logika export
                }
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Tinggi (cm)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Berat (kg)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = waist,
                    onValueChange = { waist = it },
                    label = { Text("Lingkar Pinggang (cm)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val h = height.toDoubleOrNull() ?: 0.0
                        val w = weight.toDoubleOrNull() ?: 0.0
                        if (h > 0) {
                            bmiResult = calculateBmi(w, h)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Hitung BMI")
                }

                if (bmiResult > 0) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Skor BMI Anda: ${"%.2f".format(bmiResult)}",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = "Kategori: ${getBmiCategory(bmiResult).label}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExportFAB(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Export Data"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BmiPreview() {
    BMICALCULATORTheme {
        BmiCallculator()
    }
}
