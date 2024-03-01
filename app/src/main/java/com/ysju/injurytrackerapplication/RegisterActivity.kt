package com.ysju.injurytrackerapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextEmailRegister).text.toString().trim()
            val password = findViewById<EditText>(R.id.editTextPasswordRegister).text.toString().trim()
            val confirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword).text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Passwords do not match or fields are empty", Toast.LENGTH_LONG).show()
            }
        }
    }
}
