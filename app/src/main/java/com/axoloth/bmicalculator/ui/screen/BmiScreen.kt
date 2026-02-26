package com.axoloth.bmicalculator.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.axoloth.bmicalculator.logic.getCategories
import com.axoloth.bmicalculator.ui.theme.BMICALCULATORTheme

@Composable
fun BmiCallculator(modifier: Modifier = Modifier) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf(0.0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Kalkulator BMI", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Tinggi (m)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Berat (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val h = height.toDoubleOrNull() ?: 0.0
            val w = weight.toDoubleOrNull() ?: 0.0
            if (h > 0) {
                bmiResult = w / (h * h)
            }
        }) {
            Text("Hitung BMI")
        }

        if (bmiResult > 0) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Hasil : ${"%.2f".format(bmiResult)}")
            Text(text = getCategories(bmiResult))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BMICALCULATORTheme {
        BmiCallculator()
    }
}