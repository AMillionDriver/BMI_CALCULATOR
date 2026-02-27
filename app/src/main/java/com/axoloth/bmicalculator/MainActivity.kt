package com.axoloth.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.axoloth.bmicalculator.ui.theme.BMICALCULATORTheme
import com.axoloth.bmicalculator.ui.screen.BmiCallculator
import com.axoloth.bmicalculator.ui.screen.LoginUi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isLoggedIn by remember { mutableStateOf(false) }
            var isDarkMode by remember { mutableStateOf(false) }
            
            BMICALCULATORTheme(darkTheme = isDarkMode) {
                if (!isLoggedIn) {
                    LoginUi(onLoginSuccess = { isLoggedIn = true })
                } else {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        BmiCallculator(
                            modifier = Modifier.padding(innerPadding),
                            isDarkMode = isDarkMode,
                            onDarkModeChange = { isDarkMode = it }
                        )
                    }
                }
            }
        }
    }
}
