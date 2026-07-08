package com.example.tanahku.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tanahku.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ID disesuaikan dengan layout activity_main.xml yang sudah kita perbaiki
        val btnBeliTanah = findViewById<View>(R.id.btn_lihat_semua)
        val btnJualTanah = findViewById<View>(R.id.btn_tambah_lahan)
        val btnProfile = findViewById<View>(R.id.btn_to_profile)

        btnBeliTanah?.setOnClickListener {
            startActivity(Intent(this, ListTanahActivity::class.java))
        }

        btnJualTanah?.setOnClickListener {
            startActivity(Intent(this, AddLandActivity::class.java))
        }

        btnProfile?.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }
    }
}