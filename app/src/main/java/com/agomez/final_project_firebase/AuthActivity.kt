package com.agomez.final_project_firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class AuthActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var idEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_main)

        database = FirebaseDatabase.getInstance().reference
        idEditText = findViewById(R.id.idEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signInButton = findViewById(R.id.signInButton)
        signUpButton = findViewById(R.id.signUpButton)

        signInButton.setOnClickListener {
            val id = idEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (id.isNotEmpty() && password.isNotEmpty()) {
                database.child("users").child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val storedPassword = dataSnapshot.child("password").getValue(String::class.java)
                            if (storedPassword == password) {
                                val intent = Intent(this@AuthActivity, PickUpActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@AuthActivity, "Incorrect credentials. Try again.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@AuthActivity, "Incorrect credentials. Try again.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@AuthActivity, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter ID and password.", Toast.LENGTH_SHORT).show()
            }
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}