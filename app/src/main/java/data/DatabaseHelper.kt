package com.example.tanahku.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "tanahku.db"
        const val DATABASE_VERSION = 1

        // Tabel User
        const val TABLE_USER = "users"
        const val COL_USER_ID = "id"
        const val COL_USER_NAME = "name"
        const val COL_USER_PHONE = "phone"
        const val COL_USER_EMAIL = "email"
        const val COL_USER_PASSWORD = "password"

        // Tabel Tanah
        const val TABLE_TANAH = "tanah"
        const val COL_TANAH_ID = "id"
        const val COL_TANAH_USER_ID = "user_id"
        const val COL_TANAH_NAMA = "nama_tanah"
        const val COL_TANAH_LOKASI = "lokasi"
        const val COL_TANAH_LUAS = "luas"
        const val COL_TANAH_HARGA = "harga"
        const val COL_TANAH_DESKRIPSI = "deskripsi"
        const val COL_TANAH_TANGGAL = "tanggal"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = """
            CREATE TABLE $TABLE_USER (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_NAME TEXT NOT NULL,
                $COL_USER_PHONE TEXT NOT NULL,
                $COL_USER_EMAIL TEXT NOT NULL UNIQUE,
                $COL_USER_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()

        val createTanahTable = """
            CREATE TABLE $TABLE_TANAH (
                $COL_TANAH_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TANAH_USER_ID INTEGER NOT NULL,
                $COL_TANAH_NAMA TEXT NOT NULL,
                $COL_TANAH_LOKASI TEXT NOT NULL,
                $COL_TANAH_LUAS TEXT NOT NULL,
                $COL_TANAH_HARGA TEXT NOT NULL,
                $COL_TANAH_DESKRIPSI TEXT,
                $COL_TANAH_TANGGAL TEXT NOT NULL,
                FOREIGN KEY ($COL_TANAH_USER_ID) REFERENCES $TABLE_USER($COL_USER_ID)
            )
        """.trimIndent()

        db.execSQL(createUserTable)
        db.execSQL(createTanahTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TANAH")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    // ─── USER METHODS ───────────────────────────────────────────

    fun registerUser(name: String, phone: String, email: String, password: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_USER_NAME, name)
            put(COL_USER_PHONE, phone)
            put(COL_USER_EMAIL, email)
            put(COL_USER_PASSWORD, password)
        }
        return db.insert(TABLE_USER, null, values)
    }

    fun isEmailExists(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USER,
            arrayOf(COL_USER_ID),
            "$COL_USER_EMAIL = ?",
            arrayOf(email),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun loginUser(email: String, hashedPassword: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USER,
            arrayOf(COL_USER_ID),
            "$COL_USER_EMAIL = ? AND $COL_USER_PASSWORD = ?",
            arrayOf(email, hashedPassword),
            null, null, null
        )
        val success = cursor.count > 0
        cursor.close()
        return success
    }

    fun getUserByEmail(email: String): Map<String, String>? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USER,
            null,
            "$COL_USER_EMAIL = ?",
            arrayOf(email),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            val user = mapOf(
                "id" to cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                "name" to cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME)),
                "phone" to cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PHONE)),
                "email" to cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    // ─── TANAH METHODS ──────────────────────────────────────────

    fun addTanah(
        userId: Int,
        nama: String,
        lokasi: String,
        luas: String,
        harga: String,
        deskripsi: String,
        tanggal: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TANAH_USER_ID, userId)
            put(COL_TANAH_NAMA, nama)
            put(COL_TANAH_LOKASI, lokasi)
            put(COL_TANAH_LUAS, luas)
            put(COL_TANAH_HARGA, harga)
            put(COL_TANAH_DESKRIPSI, deskripsi)
            put(COL_TANAH_TANGGAL, tanggal)
        }
        return db.insert(TABLE_TANAH, null, values)
    }

    fun getTanahByUser(userId: Int): List<Map<String, String>> {
        val db = readableDatabase
        val list = mutableListOf<Map<String, String>>()
        val cursor = db.query(
            TABLE_TANAH,
            null,
            "$COL_TANAH_USER_ID = ?",
            arrayOf(userId.toString()),
            null, null,
            "$COL_TANAH_ID DESC"
        )
        while (cursor.moveToNext()) {
            list.add(
                mapOf(
                    "id" to cursor.getString(cursor.getColumnIndexOrThrow(COL_TANAH_ID)),
                    "nama_tanah" to cursor.getString(cursor.getColumnIndexOrThrow(COL_TANAH_NAMA)),
                    "lokasi" to cursor.getString(cursor.getColumnIndexOrThrow(COL_TANAH_LOKASI)),
                    "luas" to cursor.getString(cursor.getColumnIndexOrThrow(COL_TANAH_LUAS)),
                    "harga" to cursor.getString(cursor.getColumnIndexOrThrow(COL_TANAH_HARGA)),
                    "deskripsi" to cursor.getString(cursor.getColumnIndexOrThrow(COL_TANAH_DESKRIPSI)),
                    "tanggal" to cursor.getString(cursor.getColumnIndexOrThrow(COL_TANAH_TANGGAL))
                )
            )
        }
        cursor.close()
        return list
    }

    fun deleteTanah(tanahId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_TANAH, "$COL_TANAH_ID = ?", arrayOf(tanahId.toString()))
    }

    fun updateTanah(
        tanahId: Int,
        nama: String,
        lokasi: String,
        luas: String,
        harga: String,
        deskripsi: String
    ): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TANAH_NAMA, nama)
            put(COL_TANAH_LOKASI, lokasi)
            put(COL_TANAH_LUAS, luas)
            put(COL_TANAH_HARGA, harga)
            put(COL_TANAH_DESKRIPSI, deskripsi)
        }
        return db.update(TABLE_TANAH, values, "$COL_TANAH_ID = ?", arrayOf(tanahId.toString()))
    }
}