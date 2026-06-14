# SwagLabs Mobile Automation Testing (Android)

Proyek ini dibangun untuk memenuhi tugas **Ujian Akhir Semester (UAS) mata kuliah Kualitas Perangkat Lunak**. Proyek ini mendemonstrasikan implementasi *Automation Testing* tingkat profesional untuk aplikasi mobile menggunakan **Appium 2.x**, **Java**, **Maven**, dan **TestNG** dengan pola desain **Page Object Model (POM)**.

---

## 📋 Fitur Utama & Target Pengujian

Skenario pengujian difokuskan pada fitur inti aplikasi **Sauce Labs Swag Labs Mobile App (.apk)**:
1. **User Authentication** (Login berhasil dan validasi kegagalan login dengan error message).
2. **Product List** (Memastikan katalog produk berhasil dirender).
3. **Product Cart** (Menambahkan item ke keranjang dan memverifikasi isi keranjang).
4. **Checkout Process** (Melakukan proses transaksi dari pengisian data diri hingga konfirmasi pesanan berhasil).

---

## 🛠️ Tech Stack & Library

*   **Language:** Java 17
*   **Build Tool:** Maven
*   **Automation Driver:** Appium 2.x (UiAutomator2 Driver)
*   **Testing Framework:** TestNG 7.x
*   **Design Pattern:** Page Object Model (POM) dengan *Page Chaining*
*   **Assertion Library:** TestNG Assertions

---

## 📂 Struktur Proyek

```text
SwagLabs-Mobile-Automation/
│
├── app/
│   └── SwagLabs.apk                 # Tempat file APK Swag Labs (Android) diletakkan
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── pages/               # Page Object Classes
│   │   │   │   ├── LoginPage.java
│   │   │   │   ├── ProductPage.java
│   │   │   │   ├── CartPage.java
│   │   │   │   └── CheckoutPage.java
│   │   │   │
│   │   │   └── utils/
│   │   │       └── ConfigReader.java # Pembaca properti konfigurasi
│   │   │
│   │   └── resources/
│   │       └── config.properties    # Konfigurasi Emulator & Appium Server
│   │
│   └── test/
│       └── java/
│           ├── base/
│           │   └── BaseTest.java    # Driver Setup, TearDown & Options Setup
│           │
│           └── tests/               # TestNG Test Classes
│               ├── LoginTest.java
│               ├── ProductTest.java
│               ├── CartTest.java
│               └── CheckoutTest.java
│
├── pom.xml                          # Konfigurasi dependensi Maven & plugins
├── testng.xml                       # Konfigurasi Suite Runner TestNG
├── README.md                        # Dokumentasi Proyek
└── .gitignore                       # File Ignore Version Control
```

### Penjelasan Folder & File Penting:
*   `app/`: Folder untuk menyimpan binary aplikasi Android (`SwagLabs.apk`) yang akan diuji.
*   `pages/`: Berisi class representasi dari setiap halaman visual aplikasi (Locators + Actions).
*   `utils/ConfigReader.java`: Utility untuk membaca parameter dinamis dari `config.properties`.
*   `base/BaseTest.java`: Logic setup `@BeforeClass` (inisialisasi driver dengan `UiAutomator2Options`) dan teardown `@AfterClass` (menutup sesi driver).
*   `tests/`: Berisi kode uji / assertions TestNG, tidak boleh mengandung kode locator secara langsung.
*   `testng.xml`: Menentukan urutan jalannya pengujian di seluruh kelas test suite.

---

## 📑 Spesifikasi Test Case (Test Case Specification)

| Test Case ID | Test Scenario | Preconditions | Test Steps | Expected Result | Priority |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC001** | Successful Login | App is opened. | 1. Input username `standard_user`<br>2. Input password `secret_sauce`<br>3. Klik Login | Berhasil masuk ke halaman Product List. | High |
| **TC002** | Failed Login Validation | App is opened. | 1. Input username `standard_user`<br>2. Input password salah `wrong_sauce`<br>3. Klik Login | Menampilkan pesan error validasi. | High |
| **TC003** | Product Display | User is logged in. | 1. Login dengan akun valid.<br>2. Lihat halaman utama. | Daftar produk berhasil ditampilkan. | Medium |
| **TC004** | Add Product To Cart | User is logged in. | 1. Tambah produk ke keranjang.<br>2. Buka halaman keranjang. | Produk muncul di halaman keranjang belanja. | High |
| **TC005** | Checkout Process | Product is in cart. | 1. Klik Checkout.<br>2. Isi detail nama & kodepos.<br>3. Klik Continue & Finish. | Halaman konfirmasi "THANK YOU FOR YOUR ORDER" tampil. | High |

---

## ⚙️ Persyaratan Instalasi (Prerequisites)

Pastikan komponen-komponen berikut sudah terinstal di komputer Anda:
1.  **Java Development Kit (JDK) 17**
2.  **Android Studio & Android SDK** (Serta Android Emulator yang terkonfigurasi)
3.  **Node.js** (Untuk menginstal Appium)
4.  **Appium Server 2.x & UiAutomator2 Driver**:
    ```bash
    # Menginstal Appium
    npm install -g appium

    # Menginstal UiAutomator2 driver
    appium driver install uiautomator2
    ```

### Konfigurasi Environment Variables (Windows):
Tambahkan ke System Variables Anda:
*   `JAVA_HOME`: Path ke folder instalasi JDK (contoh: `C:\Program Files\Java\jdk-17`)
*   `ANDROID_HOME`: Path ke SDK Android (contoh: `C:\Users\<Username>\AppData\Local\Android\Sdk`)
*   Tambahkan ke variabel `Path`:
    *   `%JAVA_HOME%\bin`
    *   `%ANDROID_HOME%\platform-tools`
    *   `%ANDROID_HOME%\tools`
    *   `%ANDROID_HOME%\tools\bin`

---

## 🚀 Cara Menjalankan Project

### 1. Jalankan Appium Server
Buka terminal / command prompt, lalu jalankan perintah:
```bash
appium
```
*Appium Server akan aktif di port default `http://127.0.0.1:4723`.*

### 2. Jalankan Android Emulator
Pastikan virtual device Android (Emulator) sudah menyala di Android Studio.

### 3. Siapkan File APK
Unduh file APK Swag Labs dan letakkan di dalam folder `/app/` dengan nama **`SwagLabs.apk`**.

### 4. Eksekusi Pengujian dengan Maven
Jalankan perintah berikut pada terminal proyek:

*   **Menjalankan seluruh Test Suite (Sesuai konfigurasi `testng.xml`):**
    ```bash
    mvn clean test
    ```

*   **Menjalankan kelas test tertentu saja:**
    ```bash
    mvn test -Dtest=LoginTest
    ```

---

## 📈 Desain Framework (Best Practices)
1.  **UiAutomator2Options**: Menolak penggunaan `DesiredCapabilities` yang deprecated untuk kompatibilitas jangka panjang pada Appium 2.x.
2.  **Page Chaining Pattern**: Method navigasi pada *Page Class* otomatis mengembalikan instance objek halaman berikutnya (contoh: `LoginPage.login()` mengembalikan `ProductPage`).
3.  **Explicit Waits**: Penggunaan `WebDriverWait` bersama `ExpectedConditions` secara konsisten guna meminimalisir *flakiness* tanpa membebani runtime dengan hardcoded sleep.
4.  **Clean Separation of Concerns**: Pemisahan tegas antara locator halaman (di package `pages`) dengan assert pengujian (di package `tests`).
