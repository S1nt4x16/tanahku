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
        private const val DATABASE_VERSION = 4

        // Tabel Tanah
        const val TABLE_NAME = "tanah_table"
        const val COLUMN_ID = "id"
        const val COLUMN_NAMA = "nama_lahan"
        const val COLUMN_HARGA = "harga_lahan"
        const val COLUMN_ALAMAT = "alamat_lahan"
        const val COLUMN_SERTIFIKAT = "jenis_sertifikat"
        const val COLUMN_FOTO = "foto_path"

        // Tabel User
        const val TABLE_USER = "user_table"
        const val USER_ID = "user_id"
        const val USER_NAME = "user_name"
        const val USER_PHONE = "user_phone"
        const val USER_EMAIL = "user_email"
        const val USER_PASSWORD = "user_password"

        @Volatile private var INSTANCE: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = DatabaseHelper(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTanahTable = ("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAMA TEXT, $COLUMN_HARGA TEXT, $COLUMN_ALAMAT TEXT, $COLUMN_SERTIFIKAT TEXT, $COLUMN_FOTO TEXT)")
        val createUserTable = ("CREATE TABLE $TABLE_USER ($USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_NAME TEXT, $USER_PHONE TEXT, $USER_EMAIL TEXT, $USER_PASSWORD TEXT)")
        db.execSQL(createTanahTable)
        db.execSQL(createUserTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    // --- FUNGSI USER ---
    fun registerUser(name: String, phone: String, email: String, pass: String): Long {
        val values = ContentValues().apply {
            put(USER_NAME, name)
            put(USER_PHONE, phone)
            put(USER_EMAIL, email)
            put(USER_PASSWORD, pass)
        }
        return writableDatabase.insert(TABLE_USER, null, values)
    }

    fun isEmailExists(email: String): Boolean {
        val cursor = readableDatabase.query(TABLE_USER, arrayOf(USER_ID), "$USER_EMAIL=?", arrayOf(email), null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun loginUser(email: String, pass: String): Boolean {
        val cursor = readableDatabase.query(TABLE_USER, arrayOf(USER_ID), "$USER_EMAIL=? AND $USER_PASSWORD=?", arrayOf(email, pass), null, null, null)
        val success = cursor.count > 0
        cursor.close()
        return success
    }

    // FUNGSI BARU: Ambil Data User Berdasarkan Email
    fun getUserByEmail(email: String): Map<String, String>? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USER, null, "$USER_EMAIL=?", arrayOf(email), null, null, null)

        var userData: Map<String, String>? = null
        if (cursor.moveToFirst()) {
            userData = mapOf(
                "name" to (cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME)) ?: ""),
                "phone" to (cursor.getString(cursor.getColumnIndexOrThrow(USER_PHONE)) ?: ""),
                "email" to (cursor.getString(cursor.getColumnIndexOrThrow(USER_EMAIL)) ?: "")
            )
        }
        cursor.close()
        return userData
    }

    // --- FUNGSI TANAH ---
    fun insertTanah(nama: String, harga: String, alamat: String, sertifikat: String, foto: String?): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAMA, nama); put(COLUMN_HARGA, harga); put(COLUMN_ALAMAT, alamat); put(COLUMN_SERTIFIKAT, sertifikat); put(COLUMN_FOTO, foto)
        }
        return writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun getAllTanah(): List<Land> {
        val list = mutableListOf<Land>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC", null)
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val land = Land(
                        id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                        nama = it.getString(it.getColumnIndexOrThrow(COLUMN_NAMA)),
                        harga = it.getString(it.getColumnIndexOrThrow(COLUMN_HARGA)),
                        alamat = it.getString(it.getColumnIndexOrThrow(COLUMN_ALAMAT)),
                        sertifikat = it.getString(it.getColumnIndexOrThrow(COLUMN_SERTIFIKAT)),
                        foto = it.getString(it.getColumnIndexOrThrow(COLUMN_FOTO))
                    )
                    list.add(land)
                } while (it.moveToNext())
            }
        }
        return list
    }

    fun deleteTanah(id: Int): Int = writableDatabase.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
}