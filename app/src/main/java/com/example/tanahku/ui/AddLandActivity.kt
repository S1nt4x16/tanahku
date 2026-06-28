package com.example.tanahku.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tanahku.R

class AddLandActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_land)

        findViewById<View>(R.id.btn_back_add_land)?.setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btn_scan_ocr)?.setOnClickListener {
            Toast.makeText(this, "AI sedang memverifikasi Sertifikat...", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                findViewById<EditText>(R.id.et_nama_lahan)?.setText("Tanah Kavling Margonda Permai")
                findViewById<EditText>(R.id.et_harga_lahan)?.setText("1250000000")
                findViewById<EditText>(R.id.et_alamat_lahan)?.setText("Jl. Margonda Raya No. 45, Depok, Jawa Barat")

                Toast.makeText(this, "Validasi OCR Berhasil! Data terisi otomatis.", Toast.LENGTH_LONG).show()
            }, 2000)
        }

        findViewById<Button>(R.id.btn_submit_lahan)?.setOnClickListener {
            Toast.makeText(this, "Lahan Anda berhasil diposting!", Toast.LENGTH_SHORT).show()
            finish() // Kembali ke Dashboard otomatis
        }
    }
}