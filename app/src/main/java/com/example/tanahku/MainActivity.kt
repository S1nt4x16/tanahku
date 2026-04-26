package com.example.tanahku

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- STEP 1: Munculin Halaman Login Pertama Kali ---
        showLogin()
    }

    private fun showLogin() {
        setContentView(R.layout.activity_login)

        // Cari tombol "MASUK KE TANAHKU" di activity_login.xml
        val btnLogin = findViewById<Button>(R.id.btn_login_submit)

        btnLogin.setOnClickListener {
            // Pas diklik, langsung ganti layout ke Dashboard
            showDashboard()
        }
    }

    private fun showDashboard() {
        setContentView(R.layout.activity_main)

        // Di sini nanti lo bisa tambahin logic navigasi buat
        // tombol Beli (btn_main_beli) atau Jual (btn_main_jual)
    }
}