package com.example.tanahku.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.tanahku.model.Land

class DatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TanahKu.db"
        // 1. DATABASE_VERSION dinaikkan ke 3 untuk memastikan onUpgrade dijalankan ulang jika sebelumnya ada masalah
        private const val DATABASE_VERSION = 3

        const val TABLE_NAME = "tanah_table"
        const val COLUMN_ID = "id"
        const val COLUMN_NAMA = "nama_lahan"
        const val COLUMN_HARGA = "harga_lahan"
        const val COLUMN_ALAMAT = "alamat_lahan"
        const val COLUMN_SERTIFIKAT = "jenis_sertifikat"
        const val COLUMN_FOTO = "foto_path"

        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = DatabaseHelper(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAMA TEXT," +
                "$COLUMN_HARGA TEXT," +
                "$COLUMN_ALAMAT TEXT," +
                "$COLUMN_SERTIFIKAT TEXT," +
                "$COLUMN_FOTO TEXT)")
        db.execSQL(createTable)
        Log.d("DATABASE_LOG", "Tabel Berhasil Dibuat dengan kolom foto_path")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Menghapus tabel lama dan membuat yang baru saat versi berubah
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
        Log.d("DATABASE_LOG", "Database diupgrade ke versi $newVersion")
    }

    fun insertTanah(nama: String, harga: String, alamat: String, sertifikat: String, foto: String?): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAMA, nama)
            put(COLUMN_HARGA, harga)
            put(COLUMN_ALAMAT, alamat)
            put(COLUMN_SERTIFIKAT, sertifikat)
            put(COLUMN_FOTO, foto)
        }
        val result = db.insert(TABLE_NAME, null, values)
        Log.d("DATABASE_LOG", "Insert data ID: $result")
        return result
    }

    fun getAllTanah(): List<Land> {
        val list = mutableListOf<Land>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC", null)

        // Menggunakan .use {} agar cursor otomatis tertutup dan penanganan lebih aman
        cursor?.use {
            if (it.moveToFirst()) {
                // Menggunakan getColumnIndexOrThrow setelah memastikan versi DB sudah sinkron
                val idIndex = it.getColumnIndexOrThrow(COLUMN_ID)
                val namaIndex = it.getColumnIndexOrThrow(COLUMN_NAMA)
                val hargaIndex = it.getColumnIndexOrThrow(COLUMN_HARGA)
                val alamatIndex = it.getColumnIndexOrThrow(COLUMN_ALAMAT)
                val sertifikatIndex = it.getColumnIndexOrThrow(COLUMN_SERTIFIKAT)
                val fotoIndex = it.getColumnIndexOrThrow(COLUMN_FOTO)

                do {
                    val land = Land(
                        id = it.getInt(idIndex),
                        nama = it.getString(namaIndex) ?: "",
                        harga = it.getString(hargaIndex) ?: "",
                        alamat = it.getString(alamatIndex) ?: "",
                        sertifikat = it.getString(sertifikatIndex) ?: "",
                        foto = it.getString(fotoIndex)
                    )
                    list.add(land)
                } while (it.moveToNext())
            }
        }

        return list
    }

    fun deleteTanah(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        Log.d("DATABASE_LOG", "Delete ID: $id, Result: $result")
        return result
    }
}
