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

        findViewById<View>(R.id.btn_tambah_lahan)?.setOnClickListener {
            val intent = Intent(this, AddLandActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_lihat_semua)?.setOnClickListener {
            val intent = Intent(this, ListTanahActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_to_profile)?.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }
}