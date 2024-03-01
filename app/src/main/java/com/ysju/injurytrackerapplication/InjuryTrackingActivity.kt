package com.ysju.injurytrackerapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class InjuryTrackingActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var imageViewSelectedImage: ImageView
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonSubmitInjury: Button
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageViewSelectedImage.setImageURI(uri)
            selectedImageUri = uri
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_injury_tracking)

        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        imageViewSelectedImage = findViewById(R.id.imageViewSelectedImage)
        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonSubmitInjury = findViewById(R.id.buttonSubmitInjury)

        val editTextInjuryType = findViewById<EditText>(R.id.editTextInjuryType)
        val editTextInjuryLocation = findViewById<EditText>(R.id.editTextInjuryLocation)
        val spinnerInjurySeverity = findViewById<Spinner>(R.id.spinnerInjurySeverity)

        buttonSelectImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        buttonSubmitInjury.setOnClickListener {
            val injuryType = editTextInjuryType.text.toString().trim()
            val injuryLocation = editTextInjuryLocation.text.toString().trim()
            val injurySeverity = spinnerInjurySeverity.selectedItem.toString()

            if (injuryType.isEmpty() || injuryLocation.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                selectedImageUri?.let { uri ->
                    uploadImage(uri) { imageUrl ->
                        saveInjuryData(injuryType, injuryLocation, injurySeverity, imageUrl)
                    }
                } ?: Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage(imageUri: Uri, completion: (String) -> Unit) {
        val filename = UUID.randomUUID().toString()
        val ref = storage.getReference("/injuries_images/$filename")
        Log.d("UploadPath", "Uploading to path: ${ref.path}")
        ref.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                ref.downloadUrl.addOnSuccessListener { uri ->
                    completion(uri.toString())
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveInjuryData(type: String, location: String, severity: String, imageUrl: String) {
        val injuryRecord = hashMapOf(
            "type" to type,
            "location" to location,
            "severity" to severity,
            "timestamp" to System.currentTimeMillis(),
            "imageUrl" to imageUrl
        )

        db.collection("injuries")
            .add(injuryRecord)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Injury data saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving injury data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
