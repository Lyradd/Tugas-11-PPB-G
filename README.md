# Tugas-11-PPB-G
# Starbucks Rewards App Clone (Jetpack Compose)

![Spanduk Aplikasi Starbucks](https://github.com/user-attachments/assets/08dbcddc-1b3e-4131-8b86-b8ac73da3b1e)

Sebuah klon konsep dari aplikasi seluler Starbucks, yang dibangun sepenuhnya dengan **Jetpack Compose** untuk platform Android. Proyek ini mendemonstrasikan arsitektur Android modern menggunakan Kotlin, Coroutine, dan pola MVVM untuk menciptakan antarmuka pengguna yang kaya fitur, interaktif, dan menarik secara visual.

## 🌟 Fitur

Aplikasi ini menyimulasikan fungsionalitas inti dari program Starbucks Rewards, menawarkan pengalaman pengguna yang mulus.

- **🏠 Layar Beranda**: Dasbor dinamis yang menampilkan pesan selamat datang, kartu keanggotaan pengguna dengan jumlah bintang yang dianimasikan, tindakan cepat, penawaran unggulan, dan pesanan terkini.
- **☕ Menu Pesanan**: Daftar semua produk yang tersedia yang dapat dicari, termasuk menu spesial toko.
- **🛍️ Detail & Kustomisasi Produk**: Pengguna dapat melihat detail produk dan menyesuaikan minuman mereka dengan memilih ukuran dan pilihan susu yang berbeda sebelum menambahkannya ke keranjang.
- **🛒 Keranjang Belanja**: Keranjang yang berfungsi penuh di mana pengguna dapat meninjau item, menyesuaikan jumlah, melihat total harga, dan melanjutkan ke pembayaran.
- **💳 Pembayaran & Saldo**: Menyimulasikan pembayaran dengan mengurangi saldo pengguna. Termasuk fitur "Isi Ulang" dengan beberapa pilihan metode pembayaran.
- **⭐ Sistem Hadiah**: Layar khusus untuk melihat hadiah yang tersedia. Pengguna dapat menukarkan hadiah mereka, yang akan memperbarui status mereka.
- **❤️ Favorit**: Pengguna dapat menandai produk sebagai favorit untuk akses cepat nanti dari profil mereka atau layar favorit khusus.
- **📜 Riwayat Pesanan**: Daftar komprehensif dari semua pesanan sebelumnya, yang menampilkan item, total harga, dan tanggal.
- **📍 Pencari Toko**: Daftar toko terdekat, yang menampilkan statusnya (Buka/Tutup) dan item menu spesial yang unik.
- **👤 Manajemen Profil**: Pusat utama untuk mengakses metode pembayaran, riwayat pesanan, favorit, pengaturan, dan bantuan.
- **📷 Pemindai Kode QR**:
    - Menampilkan kode QR pengguna untuk pemindaian di dalam toko.
    - Mengintegrasikan **CameraX** dan **ML Kit** untuk membuka umpan kamera langsung dan memindai kode QR untuk pembayaran.
    - Menangani izin kamera dengan baik.
- **🎨 Dukungan Tema Ganda**: Beralih dengan mulus antara **Mode Gelap** yang elegan dan **Mode Terang** klasik dari layar pengaturan.
- **🔔 Notifikasi**: Layar khusus untuk melihat semua notifikasi dan promosi terkait aplikasi.

---

## 📸 Cuplikan Layar

*(Anda dapat menambahkan cuplikan layar atau GIF Anda sendiri dari aplikasi yang sedang beraksi di sini)*

| Layar Beranda (Gelap) | Layar Pesanan | Detail Produk |
| :---: |:---:|:---:|
| <img src="https://github.com/user-attachments/assets/08dbcddc-1b3e-4131-8b86-b8ac73da3b1e" alt="Layar Beranda" width="250"/> | <img src="https://github.com/user-attachments/assets/192cbc63-b955-4f1e-922d-75d50ea493df" alt="Layar Pesanan" width="250"/> | <img src="https://github.com/user-attachments/assets/d1155f8e-df50-4c3c-aa99-0c8cea86d156" alt="Detail Produk" width="250"/> |
| **Layar Keranjang** | **Pemindai QR** | **Layar Profil** |
| <img src="https://github.com/user-attachments/assets/1ee24a22-d60a-494c-a21b-d8dd554d54ec" alt="Layar Keranjang" width="250"/> | <img src="https://github.com/user-attachments/assets/28fed553-d995-4271-b4bf-68d74bfea5ca" alt="Pemindai QR" width="250"/> | <img src="https://github.com/user-attachments/assets/f410e3b2-b7c2-43fa-b298-d9d16f178dc8" alt="Layar Profil" width="250"/> |
| **Notifikasi** | **Layar Hadiah** | **Pencari Toko** |
| <img src="https://github.com/user-attachments/assets/a3c60c08-a275-4a92-b2af-ccb29fa1c28b" alt="Notifikasi" width="250"/> | <img src="https://github.com/user-attachments/assets/16bc34d7-91e0-4157-9824-de9325fe900b" alt="Layar Hadiah" width="250"/> | <img src="https://github.com/user-attachments/assets/bda596f4-ed76-4f33-a447-a6d189b3f509" alt="Pencari Toko" width="250"/> |
| **Metode Pembayaran** | **Riwayat Pesanan** | **Pengaturan** |
| <img src="https://github.com/user-attachments/assets/9e52695d-bcae-4904-a58b-ee9be7cde6f9" alt="Metode Pembayaran" width="250"/> | <img src="https://github.com/user-attachments/assets/fc2930b3-37df-450d-9dd0-7151c5675f2e" alt="Riwayat Pesanan" width="250"/> | <img src="https://github.com/user-attachments/assets/945c70ce-fd7b-4621-a70e-88675bfdd780" alt="Pengaturan" width="250"/> |
| **Bantuan & Dukungan** | **Favorit** | |
| <img src="https://github.com/user-attachments/assets/0a4bb988-3692-4e2e-b448-fb32d7b0d222" alt="Bantuan & Dukungan" width="250"/> | <img src="https://github.com/user-attachments/assets/3f17f121-d5f9-4a14-a265-9e82479dc43b" alt="Favorit" width="250"/> | |

---

## 🛠️ Tumpukan Teknologi & Arsitektur

Proyek ini dibangun dengan tumpukan teknologi Android modern, yang menekankan kesederhanaan, skalabilitas, dan kemudahan pengujian.

- **Bahasa**: [**Kotlin**](https://kotlinlang.org/)
- **Kerangka Kerja UI**: [**Jetpack Compose**](https://developer.android.com/jetpack/compose) untuk UI deklaratif dan reaktif.
- **Arsitektur**: **MVVM (Model-View-ViewModel)**
  - **View**: Fungsi Composable di `MainActivity.kt` yang mengamati keadaan.
  - **ViewModel**: `StarbucksViewModel.kt` menyimpan semua logika bisnis dan mengekspos keadaan UI menggunakan `State<T>`, `mutableStateListOf`, dan `derivedStateOf`.
  - **Model**: Kelas data (`User`, `Product`, `Order`, dll.) yang terletak di paket `data`.
- **Manajemen Keadaan**: Menggunakan penampung keadaan asli dari Compose (`remember`, `mutableStateOf`) dan pola eksposur keadaan ViewModel.
- **Operasi Asinkron**: [**Kotlin Coroutines**](https://kotlinlang.org/docs/coroutines-overview.html) dan [**Flow**](https://kotlinlang.org/docs/flow.html) untuk mengelola tugas latar belakang, dengan `Channel` untuk peristiwa satu kali seperti menampilkan Snackbar.
- **Kamera**: [**CameraX**](https://developer.android.com/training/camerax) untuk API kamera yang sadar akan siklus hidup.
- **Pembelajaran Mesin**: [**Google ML Kit Vision**](https://developers.google.com/ml-kit/vision/barcode-scanning) untuk pemindaian kode QR yang cepat dan andal.
- **Komponen UI**: [**Material 3**](https://m3.material.io/) untuk komponen UI modern, ikon, dan tema (`lightColorScheme`, `darkColorScheme`).
- **Animasi**: Memanfaatkan API Animasi bawaan dari Compose (`AnimatedVisibility`, `animateIntAsState`, `AnimatedContent`) untuk pengalaman pengguna yang lancar.

---

## 📂 Struktur Kode

Kode diatur mengikuti praktik MVVM standar.

```
.
├── 📄 com.example.starbuckmembership
│
├── 🏠 data             # Model data (Produk, Pengguna, Pesanan, dll.)
│
├── 🧠 viewmodel
│   └── StarbucksViewModel.kt  # Menangani semua logika dan manajemen keadaan
│
└── 🎨 MainActivity.kt      # Titik masuk utama, penanganan izin, dan semua UI Composable
```
- **`MainActivity.kt`**: Aktivitas tunggal yang menampung seluruh aplikasi Jetpack Compose. Bertanggung jawab untuk meminta izin kamera dan menyiapkan composable `StarbucksApp` utama, yang berfungsi sebagai host navigasi. Semua layar dan komponen UI didefinisikan di sini sebagai fungsi `@Composable`.
- **`StarbucksViewModel.kt`**: Inti dari logika aplikasi. Mengelola keadaan untuk setiap fitur—mulai dari data pengguna dan daftar produk hingga isi keranjang belanja. Semua tindakan pengguna (misalnya, menambahkan ke keranjang, melakukan pemesanan, mengganti tema) ditangani oleh fungsi-fungsi di dalam ViewModel ini.
- **`data` package**: Berisi semua definisi `data class` Kotlin yang memodelkan data aplikasi, seperti `Product`, `User`, `Order`, `Reward`, dan `Store`.

---

## 🚀 Cara Menjalankan

Untuk menjalankan proyek ini di mesin lokal Anda, ikuti langkah-langkah berikut:

1.  **Kloning repositori:**
    ```bash
    git clone [https://github.com/username-anda/nama-repositori-anda.git](https://github.com/username-anda/nama-repositori-anda.git)
    ```
2.  **Buka di Android Studio:**
    -   Buka Android Studio (sebaiknya versi stabil terbaru).
    -   Klik `File > Open` dan pilih folder proyek yang telah dikloning.
3.  **Bangun Proyek:**
    -   Biarkan Android Studio menyinkronkan file Gradle.
    -   Bangun proyek dengan mengklik `Build > Make Project` atau menggunakan ikon palu.
4.  **Jalankan Aplikasi:**
    -   Pilih emulator atau perangkat fisik.
    -   Klik tombol `Run` (ikon putar hijau).
    -   **Catatan**: Fitur pemindai kode QR memerlukan akses kamera. Anda akan diminta untuk memberikan izin ini saat pertama kali mencoba menggunakan kamera.

---

## 📄 Lisensi

Proyek ini dilisensikan di bawah Lisensi MIT. Lihat file [LICENSE](LICENSE) untuk detailnya.

```
Hak Cipta (c) 2024 [Nama Anda]

Izin dengan ini diberikan, secara gratis, kepada siapa pun yang memperoleh salinan
perangkat lunak ini dan file dokumentasi terkait ("Perangkat Lunak"), untuk berurusan
dengan Perangkat Lunak tanpa batasan, termasuk tanpa batasan hak
untuk menggunakan, menyalin, mengubah, menggabungkan, menerbitkan, mendistribusikan, mensublisensikan, dan/atau menjual
salinan Perangkat Lunak, dan untuk mengizinkan orang yang menerima Perangkat Lunak untuk
melakukannya, dengan tunduk pada ketentuan berikut:
...
