package com.example.tanahku.ui

import com.example.tanahku.receiver.NetworkReceiver
import com.example.tanahku.R
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DetailTanahActivity : AppCompatActivity() {

    // 1. Inisialisasi Broadcast Receiver untuk cek internet
    private val networkReceiver = NetworkReceiver()

    // Hardcode nomor pemilik (Wajib diawali 62 untuk WhatsApp tanpa tanda +)
    private val nomorPemilik = "6281234567890"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tanah)

        // 2. Binding View sesuai dengan ID XML yang kamu berikan
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val ivFotoTanah = findViewById<ImageView>(R.id.iv_foto_tanah)
        val tvHargaTanah = findViewById<TextView>(R.id.tv_harga_tanah)
        val tvNamaTanah = findViewById<TextView>(R.id.tv_nama_tanah)
        val btnHubungiPemilik = findViewById<Button>(R.id.btn_hubungi_pemilik)

        // 3. Menerima data kiriman dari List (Menggunakan Explicit Intent dari tugas nurnab)
        val namaTanah = intent.getStringExtra("EXTRA_NAMA_TANAH") ?: "Margonda, Depok"
        val hargaTanah = intent.getStringExtra("EXTRA_HARGA_TANAH") ?: "Rp 2.450.000.000"
        val fotoTanahRes = intent.getIntExtra("EXTRA_FOTO_TANAH", R.drawable.lahan_kavling)

        // Set data ke komponen UI
        tvNamaTanah.text = namaTanah
        tvHargaTanah.text = hargaTanah
        ivFotoTanah.setImageResource(fotoTanahRes)

        // 4. Aksi Tombol Kembali (Back)
        btnBack.setOnClickListener {
            finish() // Menutup Activity Detail dan kembali ke List
        }

        // 5. Aksi Tombol Hubungi Pemilik -> Memunculkan Custom Dialog
        btnHubungiPemilik.setOnClickListener {
            tampilkanDialogKontak()
        }
    }

    private fun tampilkanDialogKontak() {
        // Inflate/panggil layout dialog_contact_owner.xml yang sudah kamu buat
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_contact_owner, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        val dialog = builder.create()

        // Supaya background rounded_dialog_bg milikmu terlihat sempurna (tidak terpotong kotak putih bawaan android)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Binding tombol di dalam dialog menggunakan ID XML dialogmu
        val btnWa = dialogView.findViewById<Button>(R.id.btn_wa)
        val btnTelp = dialogView.findViewById<Button>(R.id.btn_telp)

        // IMPLICIT INTENT: Buka WhatsApp
        btnWa.setOnClickListener {
            val urlWA = "https://api.whatsapp.com/send?phone=$nomorPemilik&text=Halo,%20saya%20tertarik%20dengan%20tanah%20di%20Margonda%20Depok."
            val intentWA = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(urlWA)
            }
            startActivity(intentWA)
            dialog.dismiss() // Tutup dialog setelah klik
        }

        // IMPLICIT INTENT: Telepon Langsung (Dialer)
        btnTelp.setOnClickListener {
            val intentTelp = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$nomorPemilik")
            }
            startActivity(intentTelp)
            dialog.dismiss() // Tutup dialog setelah klik
        }

        dialog.show()
    }

    // Register Broadcast Receiver secara dinamis saat halaman aktif
    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }

    // Unregister agar tidak terjadi kebocoran memori (memory leak) saat halaman ditinggalkan
    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkReceiver)
    }
}