package com.axoloth.bmicalculator.logic

/**
 * Fungsi sederhana untuk menangani proses login.
 * Untuk saat ini, fungsi ini hanya memanggil callback sukses secara langsung.
 */
fun performGoogleLogin(onLoginSuccess: () -> Unit) {
    // Implementasi login Google yang sebenarnya akan ditambahkan di sini nanti.
    // Saat ini, kita langsung memberikan akses ke aplikasi.
    onLoginSuccess()
}
