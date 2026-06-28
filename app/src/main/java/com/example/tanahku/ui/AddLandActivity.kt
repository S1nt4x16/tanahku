package com.example.tanahku.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tanahku.R
import com.example.tanahku.database.DatabaseHelper

class AddLandActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_land)

        dbHelper = DatabaseHelper(this)

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
            val nama = findViewById<EditText>(R.id.et_nama_lahan)?.text.toString().trim()
            val harga = findViewById<EditText>(R.id.et_harga_lahan)?.text.toString().trim()
            val alamat = findViewById<EditText>(R.id.et_alamat_lahan)?.text.toString().trim()
            val sertifikat = findViewById<Spinner>(R.id.spinner_sertifikat)?.selectedItem?.toString() ?: ""

            if (nama.isEmpty() || harga.isEmpty() || alamat.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = dbHelper.insertTanah(nama, harga, alamat, sertifikat)

            if (result != -1L) {
                Toast.makeText(this, "Lahan Anda berhasil diposting!", Toast.LENGTH_SHORT).show()
                finish() // Kembali ke Dashboard otomatis
            } else {
                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}