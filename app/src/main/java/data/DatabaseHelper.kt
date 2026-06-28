package com.example.tanahku.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tanahku.db"
        private const val DATABASE_VERSION = 2

        // ===== TABLE USERS =====
        const val TABLE_USERS = "users"
        const val COL_ID = "id"
        const val COL_FULL_NAME = "full_name"
        const val COL_PHONE = "phone"
        const val COL_EMAIL = "email"
        const val COL_PASSWORD = "password"
        const val COL_CREATED_AT = "created_at"

        // ===== TABLE LANDS =====
        const val TABLE_LANDS = "lands"
        const val COL_LAND_ID = "land_id"
        const val COL_LAND_NAME = "land_name"
        const val COL_LAND_PRICE = "land_price"
        const val COL_LAND_ADDRESS = "land_address"
        const val COL_USER_ID = "user_id"
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE $TABLE_USERS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_FULL_NAME TEXT NOT NULL,
                $COL_PHONE TEXT NOT NULL,
                $COL_EMAIL TEXT NOT NULL UNIQUE,
                $COL_PASSWORD TEXT NOT NULL,
                $COL_CREATED_AT TEXT NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_LANDS (
                $COL_LAND_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_LAND_NAME TEXT NOT NULL,
                $COL_LAND_PRICE TEXT NOT NULL,
                $COL_LAND_ADDRESS TEXT NOT NULL,
                $COL_USER_ID INTEGER
            )
        """.trimIndent())
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LANDS")
        onCreate(db)
    }

    // ===== REGISTER =====
    fun isEmailExists(email: String): Boolean {
        val cursor = readableDatabase.query(
            TABLE_USERS,
            arrayOf(COL_ID),
            "$COL_EMAIL=?",
            arrayOf(email),
            null,
            null,
            null
        )

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun registerUser(
        fullName: String,
        phone: String,
        email: String,
        password: String
    ): Long {

        val values = ContentValues().apply {
            put(COL_FULL_NAME, fullName)
            put(COL_PHONE, phone)
            put(COL_EMAIL, email)
            put(COL_PASSWORD, password)
            put(COL_CREATED_AT, System.currentTimeMillis().toString())
        }

        return writableDatabase.insert(
            TABLE_USERS,
            null,
            values
        )
    }

    // ===== LOGIN =====
    fun checkLogin(
        email: String,
        password: String
    ): Boolean {

        val cursor = readableDatabase.query(
            TABLE_USERS,
            arrayOf(COL_ID),
            "$COL_EMAIL=? AND $COL_PASSWORD=?",
            arrayOf(email, password),
            null,
            null,
            null
        )

        val valid = cursor.count > 0
        cursor.close()

        return valid
    }

    // ===== INSERT TANAH =====
    fun insertLand(
        landName: String,
        landPrice: String,
        landAddress: String,
        userId: Int
    ): Long {

        val values = ContentValues().apply {
            put(COL_LAND_NAME, landName)
            put(COL_LAND_PRICE, landPrice)
            put(COL_LAND_ADDRESS, landAddress)
            put(COL_USER_ID, userId)
        }

        return writableDatabase.insert(
            TABLE_LANDS,
            null,
            values
        )
    }

    // ===== AMBIL SEMUA TANAH =====
    fun getAllLands(): Cursor {
        return readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_LANDS",
            null
        )
    }

    // ===== AMBIL TANAH USER =====
    fun getUserLands(userId: Int): Cursor {
        return readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_LANDS WHERE $COL_USER_ID=?",
            arrayOf(userId.toString())
        )
    }
}