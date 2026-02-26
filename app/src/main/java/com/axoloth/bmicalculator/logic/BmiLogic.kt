package com.axoloth.bmicalculator.logic

fun getCategories(bmi: Double): String {
    return when {
        bmi < 18.5 -> "Kurus"
        bmi < 25.0 -> "Normal"
        bmi < 30.0 -> "Kelebihan Berat Badan"
        else -> "Obesitas"
    }
}