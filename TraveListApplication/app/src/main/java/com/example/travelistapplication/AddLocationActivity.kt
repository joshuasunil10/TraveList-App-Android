package com.example.travelistapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddLocationActivity : AppCompatActivity() {
    // Reference to Location DAO
    private lateinit var locationDao: LocationDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the add_location.xml layout
        setContentView(R.layout.add_location)

        // Initialize DAO
        val database = LocationDatabase.getDatabase(applicationContext)
        locationDao = database.locationDao()

        // Spinner setup
        val countrySpinner = findViewById<Spinner>(R.id.countrySpinner)
        val priorities = arrayOf("Want to Visit", "Visited") // Priority options for Spinner

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = spinnerAdapter


        // Set Save button listener
        findViewById<Button>(R.id.SaveButton).setOnClickListener {
            val locationName = findViewById<EditText>(R.id.editLocationName).text.toString().trim()
            val countryName = findViewById<EditText>(R.id.editCountryName).text.toString().trim()
            val priority = countrySpinner.selectedItem.toString()
            val description = findViewById<EditText>(R.id.editDescription).text.toString().trim()

            // Validation check
            if (locationName.isBlank() || countryName.isBlank() || description.isBlank()) {
                // Notify user of invalid input
                Toast.makeText(
                    this,
                    "All fields are required. Please fill them out.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Create Location object
            val location = Location(
                locationName = locationName,
                countryName = countryName,
                priority = priority,
                description = description
            )

            // Insert the location into the database
            lifecycleScope.launch {
                locationDao.insert(location) // Save to DB
                Toast.makeText(
                    this@AddLocationActivity,
                    "Location saved successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                finish() // Close the activity
            }
        }
    }
}
