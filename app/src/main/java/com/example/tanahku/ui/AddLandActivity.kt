package com.example.tanahku.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tanahku.R
import com.example.tanahku.database.DatabaseHelper

class AddLandActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var selectedFotoUri: String? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val ivPreview = findViewById<ImageView>(R.id.img_preview_lahan)
            val tvPlaceholder = findViewById<TextView>(R.id.tv_placeholder_hint)
            val btnRemove = findViewById<View>(R.id.btn_remove_photo)

            try {
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, takeFlags)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            ivPreview?.setImageURI(uri)
            selectedFotoUri = uri.toString()

            tvPlaceholder?.visibility = View.GONE
            btnRemove?.visibility = View.VISIBLE

        } else {
            Toast.makeText(this, "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_land)

        dbHelper = DatabaseHelper.getInstance(this)

        setupToolbar()
        setupSpinner()
        setupPhotoUpload()
        setupOCR()
        setupSubmit()
    }

    private fun setupToolbar() {
        findViewById<View>(R.id.btn_back_add_land)?.setOnClickListener {
            finish()
        }
    }

    private fun setupSpinner() {
        val spinnerSertifikat = findViewById<Spinner>(R.id.spinner_sertifikat)
        val options = arrayOf("SHM", "HGB", "Sertifikat Desa", "Lainnya")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSertifikat.adapter = adapter
    }

    private fun setupPhotoUpload() {
        val containerUpload = findViewById<View>(R.id.container_upload_photo)
        val btnRemove = findViewById<View>(R.id.btn_remove_photo)
        val tvPlaceholder = findViewById<TextView>(R.id.tv_placeholder_hint)
        val ivPreview = findViewById<ImageView>(R.id.img_preview_lahan)

        containerUpload?.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        btnRemove?.setOnClickListener {
            selectedFotoUri = null
            ivPreview?.setImageURI(null)

            tvPlaceholder?.visibility = View.VISIBLE
            btnRemove.visibility = View.GONE
        }
    }

    // ==========================================
    // SIMULASI SMART AUTO-FILL (AI OCR)
    // ==========================================
    private fun setupOCR() {
        findViewById<Button>(R.id.btn_scan_ocr)?.setOnClickListener {
            Toast.makeText(this, "AI sedang memverifikasi Dokumen/Sertifikat...", Toast.LENGTH_SHORT).show()

            // Simulasi delay biar kelihatan sistem lagi "mikir"
            Handler(Looper.getMainLooper()).postDelayed({
                findViewById<EditText>(R.id.et_nama_lahan)?.setText("Tanah Kavling Margonda Permai")
                findViewById<EditText>(R.id.et_harga_lahan)?.setText("1250000000")
                findViewById<EditText>(R.id.et_alamat_lahan)?.setText("Jl. Margonda Raya No. 45, Depok, Jawa Barat")

                Toast.makeText(this, "Validasi OCR Berhasil! Data terisi otomatis.", Toast.LENGTH_LONG).show()
            }, 2000)
        }
    }

    private fun setupSubmit() {
        findViewById<Button>(R.id.btn_submit_lahan)?.setOnClickListener {
            val nama = findViewById<EditText>(R.id.et_nama_lahan)?.text.toString().trim()
            val harga = findViewById<EditText>(R.id.et_harga_lahan)?.text.toString().trim()
            val alamat = findViewById<EditText>(R.id.et_alamat_lahan)?.text.toString().trim()
            val sertifikat = findViewById<Spinner>(R.id.spinner_sertifikat)?.selectedItem?.toString() ?: ""

            if (nama.isEmpty() || harga.isEmpty() || alamat.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = dbHelper.insertTanah(nama, harga, alamat, sertifikat, selectedFotoUri)

            if (result != -1L) {
                Toast.makeText(this, "Lahan Anda berhasil diposting!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}