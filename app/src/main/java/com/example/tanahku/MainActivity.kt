package com.example.tanahku

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.view.View

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

        // SESUAIKAN ID: Cari tombol di activity_main.xml lo yang buat ke halaman tambah
        // Misal ID-nya 'btn_tambah_lahan'
        val btnKeTambah = findViewById<View>(R.id.btn_tambah_lahan)

        btnKeTambah?.setOnClickListener {
            showAddLand()
        }
    }

    private fun showAddLand() {
        // Menampilkan layout punya orang ke-4
        setContentView(R.layout.activity_add_land)

        // ID asli dari XML orang ke-4 adalah 'btn_submit_lahan'
        val btnSubmit = findViewById<Button>(R.id.btn_submit_lahan)

        btnSubmit?.setOnClickListener {
            // Balik ke dashboard setelah klik submit
            showDashboard()
        }

        // Karena orang ke-4 gak bikin tombol BACK,
        // lo bisa pake tombol Submit buat balik sementara biar aplikasi gak stuck.
    }
}

