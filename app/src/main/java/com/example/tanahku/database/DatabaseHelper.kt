package com.example.tanahku.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tanahku.model.Land

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TanahKu.db"
        private const val DATABASE_VERSION = 2 // Diupdate karena ada perubahan skema

        // Struktur Tabel
        const val TABLE_NAME = "tanah_table"
        const val COLUMN_ID = "id"
        const val COLUMN_NAMA = "nama_lahan"
        const val COLUMN_HARGA = "harga_lahan"
        const val COLUMN_ALAMAT = "alamat_lahan"
        const val COLUMN_SERTIFIKAT = "jenis_sertifikat"
        const val COLUMN_FOTO = "foto_lahan" // Kolom baru
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAMA TEXT," +
                "$COLUMN_HARGA TEXT," +
                "$COLUMN_ALAMAT TEXT," +
                "$COLUMN_SERTIFIKAT TEXT," +
                "$COLUMN_FOTO TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_FOTO TEXT")
        }
    }

    // FUNGSI INSERT (Update: Menambahkan parameter foto)
    fun insertTanah(nama: String, harga: String, alamat: String, sertifikat: String, foto: String? = null): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAMA, nama)
            put(COLUMN_HARGA, harga)
            put(COLUMN_ALAMAT, alamat)
            put(COLUMN_SERTIFIKAT, sertifikat)
            put(COLUMN_FOTO, foto)
        }
        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success
    }

    // FUNGSI READ (Update: Mengambil data foto)
    fun getAllTanah(): List<Land> {
        val listTanah = mutableListOf<Land>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val land = Land(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)),
                    harga = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HARGA)),
                    alamat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALAMAT)),
                    sertifikat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERTIFIKAT)),
                    foto = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOTO))
                )
                listTanah.add(land)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return listTanah
    }

    // FUNGSI DELETE
    fun deleteTanah(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return success
    }
}
