package com.example.tanahku.ui

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
import com.example.tanahku.model.Land

class UserProfileActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: TanahAdapter
    private lateinit var rvMyListings: RecyclerView
    private lateinit var llEmptyListing: LinearLayout
    private lateinit var tvTotalListing: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        dbHelper = DatabaseHelper.getInstance(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_profile)
        toolbar?.setNavigationOnClickListener {
            finish()
        }

        setupRecyclerView()
        loadUserLands()

        findViewById<Button>(R.id.btn_logout)?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        rvMyListings = findViewById(R.id.rv_my_listings)
        llEmptyListing = findViewById(R.id.ll_empty_listing)
        tvTotalListing = findViewById(R.id.tv_total_listing)

        // Fix: Explicitly specify <Land> type and provide the required dbHelper argument
        adapter = TanahAdapter(mutableListOf<Land>(), dbHelper)
        rvMyListings.layoutManager = LinearLayoutManager(this)
        rvMyListings.adapter = adapter
    }

    private fun loadUserLands() {
        val listTanah = dbHelper.getAllTanah()
        
        if (listTanah.isEmpty()) {
            rvMyListings.visibility = View.GONE
            llEmptyListing.visibility = View.VISIBLE
            tvTotalListing.text = "0"
        } else {
            rvMyListings.visibility = View.VISIBLE
            llEmptyListing.visibility = View.GONE
            adapter.updateData(listTanah)
            tvTotalListing.text = listTanah.size.toString()
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserLands() // Refresh data when returning to profile
    }
}
