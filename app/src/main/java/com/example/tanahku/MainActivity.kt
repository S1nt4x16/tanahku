package com.example.tanahku

import android.content.Intent
import android.widget.Toast
import android.widget.EditText
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mulai dari Login (Atau Splash Screen kalau mau lo panggil duluan)
        showLogin()
    }

    // --- 1. HALAMAN LOGIN ---
    private fun showLogin() {
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btn_login_submit)
        val btnKeRegister = findViewById<View>(R.id.tv_to_register) // Cek ID di activity_login.xml lo

        btnLogin?.setOnClickListener { showDashboard() }

        btnKeRegister?.setOnClickListener { showRegister() }
    }

    // --- 2. HALAMAN REGISTER (Orang Baru) ---
    private fun showRegister() {
        setContentView(R.layout.activity_register)

        findViewById<View>(R.id.btn_back_register)?.setOnClickListener {
            showLogin()
        }

        // 2. Klik Link "Masuk Sekarang" di bagian bawah
        findViewById<View>(R.id.tv_go_to_login)?.setOnClickListener {
            showLogin()
        }

        // 3. Klik Tombol Daftar (Simulasi berhasil daftar langsung ke Login)
        findViewById<Button>(R.id.btn_register_submit)?.setOnClickListener {
            // Bisa tambahin Toast: Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
            showLogin()
        }
    }

    // --- 3. DASHBOARD UTAMA (Punya Lo) ---
    private fun showDashboard() {
        setContentView(R.layout.activity_main)

        // Ke Halaman Tambah Lahan (Orang ke-4)
        findViewById<View>(R.id.btn_tambah_lahan)?.setOnClickListener {
            showAddLand()
        }

        // Ke Halaman List Lahan (Orang ke-2)
        findViewById<View>(R.id.btn_lihat_semua)?.setOnClickListener {
            showListLahan()
        }

        // Ke Halaman Profile (Baru)
        findViewById<View>(R.id.btn_to_profile)?.setOnClickListener {
            showProfile() // Panggil fungsinya, jangan cuma layoutnya!
        }

    }

    // --- 4. LIST LAHAN (Orang ke-2) ---
    private fun showListLahan() {
        setContentView(R.layout.activity_list_tanah)

        // Tombol Back di Header
        findViewById<View>(R.id.btn_back_list)?.setOnClickListener {
            showDashboard()
        }

        // CARA MANGGIL TOMBOL DI DALEM INCLUDE:
        // Cari ID tombol "LIHAT DETAIL" yang ada di file 'item_card_tanah.xml'
        // Misal ID tombolnya adalah 'btn_detail_tanah'
        findViewById<Button>(R.id.btn_lihat_detail)?.setOnClickListener {
            val intent = Intent(this, DetailTanahActivity::class.java)
            startActivity(intent)
        }
    }

    // --- 5. ADD LAND (Orang ke-4) ---
    private fun showAddLand() {
        setContentView(R.layout.activity_add_land)

        // Tombol Back
        findViewById<View>(R.id.btn_back_add_land)?.setOnClickListener {
            showDashboard()
        }

        // --- LOGIKA SIMULASI OCR AI ---
        findViewById<Button>(R.id.btn_scan_ocr)?.setOnClickListener {
            Toast.makeText(this, "AI sedang memverifikasi Sertifikat...", Toast.LENGTH_SHORT).show()

            // Simulasi delay proses scanning
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                // Isi otomatis form-nya
                findViewById<EditText>(R.id.et_nama_lahan)?.setText("Tanah Kavling Margonda Permai")
                findViewById<EditText>(R.id.et_harga_lahan)?.setText("1250000000")
                findViewById<EditText>(R.id.et_alamat_lahan)?.setText("Jl. Margonda Raya No. 45, Depok, Jawa Barat")

                Toast.makeText(this, "Validasi OCR Berhasil! Data terisi otomatis.", Toast.LENGTH_LONG).show()
            }, 2000)
        }

        // Tombol Submit
        findViewById<Button>(R.id.btn_submit_lahan)?.setOnClickListener {
            showDashboard()
            Toast.makeText(this, "Lahan Anda berhasil diposting!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProfile() {
        setContentView(R.layout.activity_user_profile)

        // 2. Cari Toolbar-nya (ID: toolbar_profile ada di baris 108 XML lo)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_profile)

        // 3. SET CLIK LISTENER KHUSUS TOOLBAR (Ini buat tombol back panah itu)
        toolbar?.setNavigationOnClickListener {
            showDashboard() // Panggil fungsi dashboard lo
        }

        // 4. SET CLICK LISTENER BUAT LOGOUT (ID: btn_logout ada di baris 448 XML lo)
        findViewById<Button>(R.id.btn_logout)?.setOnClickListener {
            showLogin() // Balik ke login
        }
    }
}