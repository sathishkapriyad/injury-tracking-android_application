package com.ysju.injurytrackerapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        val userNameTextView = findViewById<TextView>(R.id.textViewUserName)
        userNameTextView.text = currentUser?.displayName ?: currentUser?.email ?: "No user information"

        // Setup click listeners for each feature card
        setupFeatureCard(R.id.cardInjuryTracking) {
            navigateToInjuryTracking()
        }
        setupFeatureCard(R.id.cardSymptomMonitoring) {
            navigateToSymptomMonitoring()
        }
        setupFeatureCard(R.id.cardConcussionAssessment) {
            navigateToConcussionAssessment()
        }
        setupFeatureCard(R.id.cardMedicalRecords) {
            navigateToMedicalRecords()
        }
    }

    private fun setupFeatureCard(cardViewId: Int, onClick: () -> Unit) {
        val cardView = findViewById<MaterialCardView>(cardViewId)
        cardView.setOnClickListener { onClick() }
    }

    private fun navigateToInjuryTracking() {
        val intent = Intent(this, InjuryTrackingActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToSymptomMonitoring() {
        val intent = Intent(this, SymptomMonitoringActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToConcussionAssessment() {
        val intent = Intent(this, ConcussionAssessmentActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMedicalRecords() {
        val intent = Intent(this, MedicalRecordsActivity::class.java)
        startActivity(intent)
    }
}
