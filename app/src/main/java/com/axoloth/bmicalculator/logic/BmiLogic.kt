package com.axoloth.bmicalculator.logic

enum class BmiCategory(val label: String) {
    UNDERWEIGHT("Kurus"),
    NORMAL("Normal"),
    OVERWEIGHT("Kelebihan Berat Badan"),
    OBESE("Obesitas")
}

fun calculateBmi(weight: Double, heightInCm: Double): Double {
    if (heightInCm <= 0) return 0.0
    val heightInMeters = heightInCm / 100
    return weight / (heightInMeters * heightInMeters)
}

fun getBmiCategory(bmi: Double): BmiCategory {
    return when {
        bmi < 18.5 -> BmiCategory.UNDERWEIGHT
        bmi < 25.0 -> BmiCategory.NORMAL
        bmi < 30.0 -> BmiCategory.OVERWEIGHT
        else -> BmiCategory.OBESE
    }
}
