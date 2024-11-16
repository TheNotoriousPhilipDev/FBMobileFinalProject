// File: app/src/main/java/com/agomez/final_project_firebase/RegistrationActivity.kt
package com.agomez.final_project_firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.appcompat.widget.Toolbar


class RegistrationActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextId: EditText
    private lateinit var buttonSave: Button
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        database = FirebaseDatabase.getInstance().reference
        editTextEmail = findViewById(R.id.editTextTextEmailAddress)
        editTextPassword = findViewById(R.id.editTextTextPassword2)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextName = findViewById(R.id.editTextTextEmailAddress3)
        editTextId = findViewById(R.id.editTextNumber3)
        buttonSave = findViewById(R.id.registerChildButton)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        buttonSave.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val phone = editTextPhone.text.toString()
            val name = editTextName.text.toString()
            val id = editTextId.text.toString()

            if (email.isEmpty() || password.isEmpty() || phone.isEmpty() || name.isEmpty() || id.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(email, password, phone, name, id)
            database.child("users").child(id).setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                    // Navigate back to AuthActivity
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

data class User(val email: String, val password: String, val phone: String, val name: String, val id: String)