// File: app/src/main/java/com/agomez/final_project_firebase/TrafficDashboardActivity.kt
package com.agomez.final_project_firebase

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.*

class TrafficDashboardActivity : AppCompatActivity() {

    private lateinit var imageButton2: ImageButton
    private lateinit var tableLayout: TableLayout
    private lateinit var toolbar: Toolbar
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic_dashboard)

        imageButton2 = findViewById(R.id.imageButton2)
        tableLayout = findViewById(R.id.tableLayout)
        database = FirebaseDatabase.getInstance().reference.child("students")
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val selectedLane = intent.getStringExtra("selectedLane")

        fetchAndPopulateTable(selectedLane)

        imageButton2.setOnClickListener {
            clearTable()
        }
    }

    private fun fetchAndPopulateTable(selectedLane: String?) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val students = snapshot.children.map { it.child("name").value.toString() }
                populateTable(selectedLane, students)       
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun populateTable(selectedLane: String?, students: List<String>) {
        if (selectedLane != null) {
            val row = TableRow(this)
            students.forEach { student ->
                val textView = TextView(this)
                textView.text = student
                textView.textSize = 20f
                textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                row.addView(textView)
            }
            tableLayout.addView(row)
        }
    }

    private fun clearTable() {
        tableLayout.removeAllViews()
    }
}