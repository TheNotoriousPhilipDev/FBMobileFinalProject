// File: app/src/main/java/com/agomez/final_project_firebase/PickUpActivity.kt
package com.agomez.final_project_firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PickUpActivity : AppCompatActivity() {

    private lateinit var checkBox1: CheckBox
    private lateinit var checkBox2: CheckBox
    private lateinit var checkBox3: CheckBox
    private lateinit var signOutButton: Button
    private lateinit var registerChildButton: Button
    private lateinit var imageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_up)

        checkBox1 = findViewById(R.id.checkBox1)
        checkBox2 = findViewById(R.id.checkBox2)
        checkBox3 = findViewById(R.id.checkBox3)
        registerChildButton = findViewById(R.id.registerChildButton)
        imageButton = findViewById(R.id.imageButton)
        signOutButton = findViewById(R.id.button5)

        val checkBoxes = listOf(checkBox1, checkBox2, checkBox3)

        checkBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkBoxes.filter { it != checkBox }.forEach { it.isChecked = false }
                }
            }
        }

        registerChildButton.setOnClickListener {
            val intent = Intent(this, ChildRegistrationActivity::class.java)
            startActivity(intent)
        }

        imageButton.setOnClickListener {
            val selectedLane = when {
                checkBox1.isChecked -> "lane1"
                checkBox2.isChecked -> "lane2"
                checkBox3.isChecked -> "lane3"
                else -> null
            }
            if (selectedLane != null) {
                val intent = Intent(this, TrafficDashboardActivity::class.java)
                intent.putExtra("selectedLane", selectedLane)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select a lane", Toast.LENGTH_SHORT).show()
            }
        }

        signOutButton.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}