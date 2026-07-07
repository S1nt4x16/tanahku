package com.example.tanahku

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.et_full_name)
        etPhone = findViewById(R.id.et_phone)
        etEmail = findViewById(R.id.et_email_register)
        etPassword = findViewById(R.id.et_password_register)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        btnRegister = findViewById(R.id.btn_register_submit)

        btnRegister.setOnClickListener {

            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val confirm = etConfirmPassword.text.toString()

            if (name.isEmpty()) {
                etName.error = "Nama wajib diisi"
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                etPhone.error = "Nomor telepon wajib diisi"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                etEmail.error = "Email wajib diisi"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Password wajib diisi"
                return@setOnClickListener
            }

            if (password != confirm) {
                Toast.makeText(
                    this,
                    "Password tidak sama",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Registrasi Berhasil",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}