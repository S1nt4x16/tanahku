package com.example.tanahku.utils
// ⚠️ GANTI: com.contoh.tanahku sesuai package name lo

import java.security.MessageDigest

object PasswordUtils {

    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun isPasswordStrong(password: String): Boolean {
        val pattern = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,}\$")
        return pattern.matches(password)
    }
}