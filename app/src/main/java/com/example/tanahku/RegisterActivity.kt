package com.example.tanahku

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etKonfirmasi: EditText
    private lateinit var ivTogglePass: ImageView
    private lateinit var ivToggleKonfirm: ImageView
    private lateinit var tvPasswordError: TextView
    private lateinit var cbTerms: CheckBox
    private lateinit var btnDaftar: Button
    private lateinit var tvKeLogin: TextView
    private lateinit var btnBack: ImageView

    private var isPasswordVisible = false
    private var isKonfirmasiVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        etNama          = findViewById(R.id.et_full_name)
        etPhone         = findViewById(R.id.et_phone)
        etEmail         = findViewById(R.id.et_email_register)
        etPassword      = findViewById(R.id.et_password_register)
        etKonfirmasi    = findViewById(R.id.et_confirm_password)
        ivTogglePass    = findViewById(R.id.iv_toggle_password_register)
        ivToggleKonfirm = findViewById(R.id.iv_toggle_confirm_password)
        tvPasswordError = findViewById(R.id.tv_password_error)
        cbTerms         = findViewById(R.id.cb_terms)
        btnDaftar       = findViewById(R.id.btn_register_submit)
        tvKeLogin       = findViewById(R.id.tv_go_to_login)
        btnBack         = findViewById(R.id.btn_back_register)

        btnBack.setOnClickListener { finish() }

        ivTogglePass.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            etPassword.transformationMethod = if (isPasswordVisible)
                HideReturnsTransformationMethod.getInstance()
            else
                PasswordTransformationMethod.getInstance()
            etPassword.setSelection(etPassword.text.length)
        }

        ivToggleKonfirm.setOnClickListener {
            isKonfirmasiVisible = !isKonfirmasiVisible
            etKonfirmasi.transformationMethod = if (isKonfirmasiVisible)
                HideReturnsTransformationMethod.getInstance()
            else
                PasswordTransformationMethod.getInstance()
            etKonfirmasi.setSelection(etKonfirmasi.text.length)
        }

        btnDaftar.setOnClickListener { prosesRegister() }

        tvKeLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun prosesRegister() {
        val nama       = etNama.text.toString().trim()
        val phone      = etPhone.text.toString().trim()
        val email      = etEmail.text.toString().trim()
        val password   = etPassword.text.toString().trim()
        val konfirmasi = etKonfirmasi.text.toString().trim()

        tvPasswordError.visibility = View.GONE

        if (nama.isEmpty() || phone.isEmpty() || email.isEmpty()
            || password.isEmpty() || konfirmasi.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 8) {
            Toast.makeText(this, "Password minimal 8 karakter!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != konfirmasi) {
            tvPasswordError.visibility = View.VISIBLE
            return
        }

        if (!cbTerms.isChecked) {
            Toast.makeText(this, "Harap setujui syarat & ketentuan!", Toast.LENGTH_SHORT).show()
            return
        }

        // Sementara langsung pindah ke Login dulu
        // DatabaseHelper akan diintegrasikan setelah merge
        Toast.makeText(this, "Daftar berhasil! Silakan login.", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}