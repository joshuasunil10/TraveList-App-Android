package com.example.travelistapplication

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailLocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Set the content view
        setContentView(R.layout.activity_detail_location)

        // Get the passed ID
        val locationId = intent.getIntExtra("LOCATION_ID", -1)
        if (locationId != -1) {
            // Fetch the details of the location
            fetchAndDisplayLocation(locationId)
        }

        // Set up the view map button
        val viewMapButton = findViewById<Button>(R.id.viewMapButton)
        viewMapButton.setOnClickListener {
            val locationId = intent.getIntExtra("LOCATION_ID", -1)
            if (locationId != -1) {
                // Reference: The following code snippet is from Gemini, my prompt was How to create a function to pass the country name from my room database to Google Maps to search for the country?
                openMap(locationId)
                // Reference Complete
            }
        }

        // Set up the edit location button
        val editLocationButton = findViewById<Button>(R.id.EditButton)
        editLocationButton.setOnClickListener {
            val locationId = intent.getIntExtra("LOCATION_ID", -1)
            if (locationId != -1) {
                showEditPriorityDialog(locationId)
            }
        }
    }

    // Fetch location details from the database and display them
    private fun fetchAndDisplayLocation(locationId: Int) {
        val database = LocationDatabase.getDatabase(this)
        val dao = database.locationDao()

        // Use a coroutine to fetch data from Room database
        CoroutineScope(Dispatchers.IO).launch {
            val location = dao.getItemById(locationId)
            runOnUiThread {
                findViewById<TextView>(R.id.LocationName).text = location?.locationName ?: "N/A"
                findViewById<TextView>(R.id.textCountry).text = location?.countryName ?: "N/A"
                findViewById<TextView>(R.id.textPriority).text = location?.priority ?: "N/A"
                findViewById<TextView>(R.id.textDescription).text = location?.description ?: "N/A"
            }
        }
    }

    // Reference: The following code snippet is from Gemini, my prompt was
    // How to create a function to pass the country name from my room database to Google Maps to search for the country?

    // Open Google Maps to search by country name
    private fun openMap(locationId: Int) {
        val database = LocationDatabase.getDatabase(this)
        val dao = database.locationDao()

        CoroutineScope(Dispatchers.IO).launch {
            val location = dao.getItemById(locationId)

            location?.let {
                val locationName = it.locationName

                if (locationName.isNotEmpty()) {
                    // Construct a URI for Google Maps search
                    val searchQuery = Uri.encode(locationName)
                    val mapUri = Uri.parse("geo:0,0?q=$searchQuery")
                    val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                }
            }
        }
    }

    // Reference Complete

    // dialog to select new priority (Want to Visit / Visited)
    private fun showEditPriorityDialog(locationId: Int) {
        val options = arrayOf("Want to Visit", "Visited")
        var selectedPriorityIndex = 0  // Default index (0 for "Want to Visit")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Travel Priority")

        // Use setSingleChoiceItems to display radio buttons
        builder.setSingleChoiceItems(options, selectedPriorityIndex) { dialog, which ->
            selectedPriorityIndex = which // Update the selected index
        }

        // Set positive button to update priority
        builder.setPositiveButton("OK") { dialog, which ->
            val newPriority = options[selectedPriorityIndex]
            updateLocationPriority(locationId, newPriority)
        }

        // Set negative button to cancel the operation
        builder.setNegativeButton("Cancel", null)

        builder.show()
    }

    // Update location's priority in the database
    private fun updateLocationPriority(locationId: Int, newPriority: String) {
        val database = LocationDatabase.getDatabase(this)
        val dao = database.locationDao()

        CoroutineScope(Dispatchers.IO).launch {
            // Get the location from the database
            val location = dao.getItemById(locationId)

            location?.let {
                // Create a new location object with the updated priority
                val updatedLocation = it.copy(priority = newPriority)

                // Update the location in the database with the new object
                dao.update(updatedLocation)

                // Update the UI with the new priority
                runOnUiThread {
                    findViewById<TextView>(R.id.textPriority).text = newPriority
                }
            }
        }
    }
}
