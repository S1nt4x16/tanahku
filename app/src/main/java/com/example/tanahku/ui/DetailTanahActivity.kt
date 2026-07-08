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

    private val networkReceiver = NetworkReceiver()
    private val nomorPemilik = "6281234567890"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tanah)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val ivFotoTanah = findViewById<ImageView>(R.id.iv_foto_tanah)
        val tvHargaTanah = findViewById<TextView>(R.id.tv_harga_tanah)
        val tvNamaTanah = findViewById<TextView>(R.id.tv_nama_tanah)
        val btnHubungiPemilik = findViewById<Button>(R.id.btn_hubungi_pemilik)

        // TERIMA DATA DARI ADAPTER
        val namaTanah = intent.getStringExtra("EXTRA_NAMA_TANAH") ?: "Tanah Tidak Diketahui"
        val hargaTanah = intent.getStringExtra("EXTRA_HARGA_TANAH") ?: "Rp 0"
        val fotoTanahUri = intent.getStringExtra("EXTRA_FOTO_URI")

        tvNamaTanah.text = namaTanah
        tvHargaTanah.text = hargaTanah

        // Set gambar dari Database (URI)
        if (!fotoTanahUri.isNullOrEmpty()) {
            try {
                ivFotoTanah.setImageURI(Uri.parse(fotoTanahUri))
            } catch (e: Exception) {
                ivFotoTanah.setImageResource(R.drawable.lahan_kavling) // Fallback statis
            }
        } else {
            ivFotoTanah.setImageResource(R.drawable.lahan_kavling)
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnHubungiPemilik.setOnClickListener {
            tampilkanDialogKontak(namaTanah)
        }
    }

    private fun tampilkanDialogKontak(namaTanah: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_contact_owner, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnWa = dialogView.findViewById<Button>(R.id.btn_wa)
        val btnTelp = dialogView.findViewById<Button>(R.id.btn_telp)

        // WA Text Dinamis sesuai tanah yang di-klik
        btnWa.setOnClickListener {
            val pesan = "Halo, saya tertarik dengan tanah di ${namaTanah}."
            val urlWA = "https://api.whatsapp.com/send?phone=$nomorPemilik&text=${Uri.encode(pesan)}"

            val intentWA = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(urlWA) }
            startActivity(intentWA)
            dialog.dismiss()
        }

        btnTelp.setOnClickListener {
            val intentTelp = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:$nomorPemilik") }
            startActivity(intentTelp)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkReceiver)
    }
}