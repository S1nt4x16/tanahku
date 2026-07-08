package com.example.tanahku.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tanahku.R
import com.example.tanahku.database.DatabaseHelper // <-- INI UDAH DIBENERIN KE FOLDER DATABASE
import com.example.tanahku.utils.PasswordUtils

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var cbTerms: CheckBox
    private lateinit var tvTermsLink: TextView
    private lateinit var tvPasswordError: TextView
    private lateinit var btnBack: ImageView
    private lateinit var tvGoToLogin: TextView
    private lateinit var ivTogglePassword: ImageView
    private lateinit var ivToggleConfirmPassword: ImageView

    private lateinit var dbHelper: DatabaseHelper

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // INI UDAH DIBENERIN PAKE getInstance()
        dbHelper = DatabaseHelper.getInstance(this)

        // ID sesuai activity_register.xml yang ada di project
        etName = findViewById(R.id.et_full_name)
        etPhone = findViewById(R.id.et_phone)
        etEmail = findViewById(R.id.et_email_register)
        etPassword = findViewById(R.id.et_password_register)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        btnRegister = findViewById(R.id.btn_register_submit)
        cbTerms = findViewById(R.id.cb_terms)
        tvTermsLink = findViewById(R.id.tv_terms_link)
        tvPasswordError = findViewById(R.id.tv_password_error)
        btnBack = findViewById(R.id.btn_back_register)
        tvGoToLogin = findViewById(R.id.tv_go_to_login)
        ivTogglePassword = findViewById(R.id.iv_toggle_password_register)
        ivToggleConfirmPassword = findViewById(R.id.iv_toggle_confirm_password)

        btnBack.setOnClickListener { finish() }

        tvGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        tvTermsLink.setOnClickListener {
            Toast.makeText(this, "Buka halaman Syarat & Ketentuan", Toast.LENGTH_SHORT).show()
        }

        ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(etPassword, ivTogglePassword, isPasswordVisible)
        }

        ivToggleConfirmPassword.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            togglePasswordVisibility(etConfirmPassword, ivToggleConfirmPassword, isConfirmPasswordVisible)
        }

        btnRegister.setOnClickListener { handleRegister() }
    }

    private fun togglePasswordVisibility(editText: EditText, toggleIcon: ImageView, visible: Boolean) {
        editText.inputType = if (visible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        toggleIcon.setImageResource(
            if (visible) android.R.drawable.ic_menu_close_clear_cancel
            else android.R.drawable.ic_menu_view
        )
        editText.setSelection(editText.text?.length ?: 0)
    }

    private fun handleRegister() {
        tvPasswordError.visibility = android.view.View.GONE

        val name = etName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val confirm = etConfirmPassword.text.toString()

        if (name.isEmpty()) {
            etName.error = "Nama wajib diisi"
            etName.requestFocus()
            return
        }
        if (phone.isEmpty()) {
            etPhone.error = "Nomor telepon wajib diisi"
            etPhone.requestFocus()
            return
        }
        if (phone.length < 10 || phone.length > 13) {
            etPhone.error = "Nomor telepon tidak valid"
            etPhone.requestFocus()
            return
        }
        if (email.isEmpty()) {
            etEmail.error = "Email wajib diisi"
            etEmail.requestFocus()
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Format email tidak valid"
            etEmail.requestFocus()
            return
        }
        if (password.isEmpty()) {
            etPassword.error = "Password wajib diisi"
            etPassword.requestFocus()
            return
        }
        if (!PasswordUtils.isPasswordStrong(password)) {
            etPassword.error = "Min. 8 karakter, huruf besar, kecil, angka & simbol"
            etPassword.requestFocus()
            return
        }
        if (confirm.isEmpty()) {
            etConfirmPassword.error = "Konfirmasi password wajib diisi"
            etConfirmPassword.requestFocus()
            return
        }
        if (password != confirm) {
            tvPasswordError.visibility = android.view.View.VISIBLE
            etConfirmPassword.requestFocus()
            return
        }
        if (!cbTerms.isChecked) {
            Toast.makeText(this, "Setujui syarat & ketentuan dulu", Toast.LENGTH_SHORT).show()
            return
        }
        if (dbHelper.isEmailExists(email)) {
            etEmail.error = "Email sudah terdaftar"
            Toast.makeText(this, "Email sudah digunakan", Toast.LENGTH_SHORT).show()
            return
        }

        val hashedPassword = PasswordUtils.hashPassword(password)
        val result = dbHelper.registerUser(name, phone, email, hashedPassword)

        if (result != -1L) {
            Toast.makeText(this, "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Registrasi gagal, coba lagi", Toast.LENGTH_SHORT).show()
        }
    }
}