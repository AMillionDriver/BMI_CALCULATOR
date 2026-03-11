package com.axoloth.bmicalculator.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onDarkModeChange: (Boolean) -> Unit = {},
    onNavigateToShare: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    isLoggedIn: Boolean = false
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
                    label = { Text(if (isLoggedIn) "Logout" else "Login") },
                    selected = false,
                    icon = { 
                        Icon(
                            imageVector = if (isLoggedIn) Icons.AutoMirrored.Filled.Logout else Icons.Default.Login, 
                            contentDescription = null
                        ) 
                    },
                    onClick = { 
                        scope.launch { drawerState.close() }
                        onNavigateToProfile()
                    }
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
            bottomBar = {
                BmiBottomNavigationBar(
                    onHistoryClick = onNavigateToHistory,
                    onAccountClick = onNavigateToProfile
                )
            },
            floatingActionButton = {
                ExportFAB(onClick = onNavigateToShare)
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
                    modifier = Modifier
                        .widthIn(max = 488.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Berat (kg)") },
                    modifier = Modifier
                        .widthIn(max = 488.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = waist,
                    onValueChange = { waist = it },
                    label = { Text("Lingkar Pinggang (cm)") },
                    modifier = Modifier
                        .widthIn(max = 488.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
                    modifier = Modifier
                        .widthIn(max = 320.dp)
                        .fillMaxWidth()
                ) {
                    Text("Hitung BMI")
                }

                if (bmiResult > 0) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        modifier = Modifier
                            .widthIn(max = 488.dp)
                            .fillMaxWidth(),
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
fun BmiBottomNavigationBar(
    onHistoryClick: () -> Unit = {},
    onAccountClick: () -> Unit = {}
) {
    var selectedItem by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            color = Color.Black
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    icon = Icons.Default.Home,
                    label = "HOME",
                    isSelected = selectedItem == 0,
                    onClick = { selectedItem = 0 }
                )
                BottomNavItem(
                    icon = Icons.Default.History,
                    label = "HISTORY",
                    isSelected = selectedItem == 1,
                    onClick = { 
                        selectedItem = 1
                        onHistoryClick()
                    }
                )
                BottomNavItem(
                    icon = Icons.Default.AccountCircle,
                    label = "ACCOUNT",
                    isSelected = selectedItem == 2,
                    isSpecial = true,
                    onClick = { 
                        selectedItem = 2 
                        onAccountClick()
                    }
                )
                BottomNavItem(
                    icon = Icons.Default.FitnessCenter,
                    label = "EXERCISE",
                    isSelected = selectedItem == 3,
                    onClick = { selectedItem = 3 }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isSpecial: Boolean = false
) {
    val scale by animateFloatAsState(if (isSelected) 1.2f else 1.0f, label = "scale")
    val iconColor by animateColorAsState(if (isSelected) Color(0xFF3B82F6) else Color.White, label = "color")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .scale(scale)
    ) {
        if (isSpecial) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        if (isSelected) Color.White else Color(0xFF3B82F6),
                        shape = RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) Color(0xFF3B82F6) else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            color = iconColor,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold
        )
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
