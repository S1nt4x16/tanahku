package com.example.tanahku.ui

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tanahku.R

class DetailTanahActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tanah)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnHubungi = findViewById<Button>(R.id.btn_hubungi_pemilik)

        btnBack.setOnClickListener {
            finish()
        }

        btnHubungi.setOnClickListener {
            showContactDialog()
        }
    }

    private fun showContactDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_contact_owner)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnWa = dialog.findViewById<Button>(R.id.btn_wa)
        val btnTelp = dialog.findViewById<Button>(R.id.btn_telp)
        val nomorTujuan = "081234567890"

        btnWa.setOnClickListener {
            val pesan = "Halo, saya tertarik dengan tanah yang diiklankan di aplikasi."
            val url = "https://api.whatsapp.com/send?phone=\$nomorTujuan&text=\${Uri.encode(pesan)}"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            try {
                startActivity(intent)
            } catch (e : Exception) {
                Toast.makeText(this, "WhatsApp tidak tersedia di HP ini", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        btnTelp.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("Tel:+$nomorTujuan")
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }
}