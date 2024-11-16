package com.agomez.final_project_firebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.*

class ChildRegistrationActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var editTextName: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var registerButton: Button
    private lateinit var tableLayout: TableLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_registration)

        database = FirebaseDatabase.getInstance().reference.child("students")
        editTextName = findViewById(R.id.editTextText2)
        radioGroup = findViewById(R.id.radioGroup)
        registerButton = findViewById(R.id.registerButton)
        tableLayout = findViewById(R.id.tableLayout)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        registerButton.setOnClickListener {
            val name = editTextName.text.toString()
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId

            if (name.isEmpty() || selectedRadioButtonId == -1) {
                Toast.makeText(this, "Please enter the name and select a section", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
            val section = selectedRadioButton.text.toString()

            val student = Student(name, section)
            val newStudentRef = database.push()
            newStudentRef.setValue(student).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Student registered successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fetchStudents()
    }

    private fun fetchStudents() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clearTable()
                for (studentSnapshot in snapshot.children) {
                    val student = studentSnapshot.getValue(Student::class.java)
                    student?.let {
                        addStudentToTable(it, studentSnapshot.key)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChildRegistrationActivity, "Failed to load students: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addStudentToTable(student: Student, studentKey: String?) {
        val tableRow = TableRow(this)
        val nameTextView = TextView(this)
        val sectionTextView = TextView(this)
        val actionButton = Button(this)

        nameTextView.text = student.name
        sectionTextView.text = student.section
        actionButton.text = "Delete"
        actionButton.setOnClickListener {
            studentKey?.let {
                database.child(it).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Student deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to delete student: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        tableRow.addView(nameTextView)
        tableRow.addView(sectionTextView)
        tableRow.addView(actionButton)

        tableLayout.addView(tableRow)
    }

    private fun clearTable() {
        tableLayout.removeViews(1, tableLayout.childCount - 1)
    }
}