package com.axoloth.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.axoloth.bmicalculator.ui.theme.BMICALCULATORTheme
import com.axoloth.bmicalculator.ui.screen.BmiCallculator
import com.axoloth.bmicalculator.ui.screen.LoginUi
import com.axoloth.bmicalculator.ui.screen.ShareScreen
import com.axoloth.bmicalculator.ui.screen.HistoryScreen
import com.axoloth.bmicalculator.ui.screen.ProfileScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val auth = FirebaseAuth.getInstance()
            var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }
            var isDarkMode by remember { mutableStateOf(false) }
            val navController = rememberNavController()
            
            BMICALCULATORTheme(darkTheme = isDarkMode) {
                if (!isLoggedIn) {
                    LoginUi(onLoginSuccess = { isLoggedIn = true })
                } else {
                    NavHost(navController = navController, startDestination = "bmi_calculator") {
                        composable("bmi_calculator") {
                            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                BmiCallculator(
                                    modifier = Modifier.padding(innerPadding),
                                    isDarkMode = isDarkMode,
                                    onDarkModeChange = { isDarkMode = it },
                                    onNavigateToShare = { navController.navigate("share_screen") },
                                    onNavigateToHistory = { navController.navigate("history_screen") },
                                    onNavigateToProfile = { navController.navigate("profile_screen") },
                                    isLoggedIn = isLoggedIn
                                )
                            }
                        }
                        composable("share_screen") {
                            ShareScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable("history_screen") {
                            HistoryScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable("profile_screen") {
                            ProfileScreen(
                                onBackClick = { navController.popBackStack() },
                                onLogoutClick = { 
                                    isLoggedIn = false
                                    navController.navigate("bmi_calculator") {
                                        popUpTo(0)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BmiPreview() {
    BMICALCULATORTheme {
        BmiCallculator()
    }
}
