# 📱 Kalkulator BMI (Android Jetpack Compose)

Aplikasi modern untuk menghitung *Body Mass Index* (BMI) dengan akurasi tinggi. Project ini dibangun menggunakan **Jetpack Compose** untuk mengeksplorasi arsitektur UI deklaratif dan pemisahan logika bisnis yang bersih di Android.

---

## 📸 Tampilan Aplikasi

| Light Mode | Dark Mode | Input Data | Hasil Perhitungan |
|:---:|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/82b4d78d-83b1-41cd-a21d-7ec3ac3b7d6e" width="200"> | <img src="https://github.com/user-attachments/assets/91d874e8-c12b-48f5-986b-9625920f195d" width="200"> | <img src="https://github.com/user-attachments/assets/5d031b41-0775-4c8d-a03b-e36dcd6bfea7" width="200"> | <img src="https://github.com/user-attachments/assets/d2c4b0ad-0573-408e-bffc-4f8dd3c642e9" width="200"> |

---

## ✨ Fitur Utama
- **Real-time Calculation:** Hasil BMI muncul seketika saat data diinput.
- **Dynamic Categories:** Klasifikasi berat badan otomatis (Kurus, Normal, Ideal, atau Obesitas).
- **System-based Dark Mode:** Tema aplikasi yang otomatis menyesuaikan pengaturan perangkat.
- **Improved Accuracy:** Implementasi logika tambahan untuk hasil yang lebih presisi.
- **Material Design 3:** Antarmuka modern, bersih, dan responsif.

## 🛠️ Tech Stack
- **Kotlin** - Bahasa pemrograman utama.
- **Jetpack Compose** - Toolkit modern untuk membangun UI secara deklaratif.
- **Material 3** - Desain sistem terbaru dari Google untuk komponen UI.

## 📁 Arsitektur & Struktur Folder
Project ini menerapkan prinsip *Separation of Concerns* (SoC) agar kode mudah di-*maintain*:
- `ui.screen` : Berisi komponen UI (*Composables*) seperti `BmiScreen.kt`.
- `logic` : Berisi logika perhitungan murni (Pure Functions) di `BmiLogic.kt`.
- `ui.theme` : Konfigurasi tema, palet warna, dan tipografi aplikasi.

## 🚀 Cara Menjalankan
1. Clone repository ini:
   ```bash
   git clone [https://github.com/AMillionDriver/BMI_CALCULATOR.git](https://github.com/AMillionDriver/BMI_CALCULATOR.git)
