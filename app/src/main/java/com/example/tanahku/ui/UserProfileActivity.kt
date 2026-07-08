package com.example.tanahku.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tanahku.R
import com.example.tanahku.adapter.TanahAdapter
import com.example.tanahku.database.DatabaseHelper

class UserProfileActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: TanahAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        dbHelper = DatabaseHelper.getInstance(this)

        // 1. Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar_profile)
        setSupportActionBar(toolbar)

        // TAMBAHIN BARIS INI BUAT NGILANGIN TULISAN "TanahKu"
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar?.setNavigationOnClickListener { finish() }

        // 2. Load User Data dari Database
        loadUserData()

        // 3. Setup RecyclerView buat list tanah
        val rv = findViewById<RecyclerView>(R.id.rv_my_listings)
        adapter = TanahAdapter(mutableListOf(), dbHelper)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // 4. Fitur Logout (Hapus session)
        findViewById<Button>(R.id.btn_logout)?.setOnClickListener {
            getSharedPreferences("TanahKuSession", Context.MODE_PRIVATE).edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        loadData()
    }

    private fun loadUserData() {
        // Ambil email user yang lagi login
        val sharedPref = getSharedPreferences("TanahKuSession", Context.MODE_PRIVATE)
        val emailSession = sharedPref.getString("USER_EMAIL", "") ?: ""

        if (emailSession.isNotEmpty()) {
            val userData = dbHelper.getUserByEmail(emailSession)
            if (userData != null) {
                // Tembak data ke XML
                findViewById<TextView>(R.id.tv_profile_name)?.text = userData["name"]
                findViewById<TextView>(R.id.tv_profile_email)?.text = userData["email"]

                findViewById<TextView>(R.id.tv_info_name)?.text = userData["name"]
                findViewById<TextView>(R.id.tv_info_phone)?.text = userData["phone"]
                findViewById<TextView>(R.id.tv_info_email)?.text = userData["email"]
            }
        }
    }

    private fun loadData() {
        val listTanah = dbHelper.getAllTanah()
        val llEmpty = findViewById<LinearLayout>(R.id.ll_empty_listing)
        val tvTotal = findViewById<TextView>(R.id.tv_total_listing)
        val rv = findViewById<RecyclerView>(R.id.rv_my_listings)

        if (listTanah.isEmpty()) {
            rv.visibility = View.GONE
            llEmpty.visibility = View.VISIBLE
            tvTotal.text = "0"
        } else {
            rv.visibility = View.VISIBLE
            llEmpty.visibility = View.GONE
            adapter.updateData(listTanah)
            tvTotal.text = listTanah.size.toString()
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
        loadUserData() // Refresh data user juga barangkali butuh
    }
}