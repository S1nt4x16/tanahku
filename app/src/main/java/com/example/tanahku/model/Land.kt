package com.example.tanahku.model

data class Land(
    val id: Int? = null,
    val nama: String,
    val harga: String,
    val alamat: String,
    val sertifikat: String,
    // 1. Properti foto adalah Nullable String agar aman jika data kosong
    val foto: String? = null
)
