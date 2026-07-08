package com.example.tanahku.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tanahku.R
import com.example.tanahku.adapter.TanahAdapter
import com.example.tanahku.database.DatabaseHelper

class ListTanahActivity : AppCompatActivity() {

    private lateinit var rvTanah: RecyclerView
    private lateinit var adapter: TanahAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_tanah)

        // Inisialisasi Database Helper
        dbHelper = DatabaseHelper.getInstance(this)

        // Setup RecyclerView
        rvTanah = findViewById(R.id.rv_list_tanah)
        rvTanah.layoutManager = LinearLayoutManager(this)

        // Tombol Back
        findViewById<ImageButton>(R.id.btn_back_list)?.setOnClickListener {
            finish()
        }
    }

    // Pakai onResume biar pas user abis nambah data, layarnya otomatis refresh
    override fun onResume() {
        super.onResume()
        loadDataDariDatabase()
    }

    private fun loadDataDariDatabase() {
        // Tarik semua data dari SQLite
        val listTanahAsli = dbHelper.getAllTanah()

        // Suapin datanya ke Adapter
        adapter = TanahAdapter(listTanahAsli.toMutableList(), dbHelper)
        rvTanah.adapter = adapter

        // (Opsional) Bikin tombol lihat detail di adapter hidup kalau mau di klik
        // Nanti lu bisa atur klik per item di adapter kalau butuh pindah ke DetailTanahActivity
    }
}