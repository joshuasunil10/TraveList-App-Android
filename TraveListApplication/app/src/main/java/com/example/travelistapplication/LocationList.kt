package com.example.travelistapplication

// displays the list of locations in the app as a recycler view, and allows the user to add new locations


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LocationList : AppCompatActivity() {

    private lateinit var addItemLauncher: ActivityResultLauncher<Intent>
    private lateinit var locationAdapter: LocationAdapter
    // Database Access Object for Location Entity
    private lateinit var locationDao: LocationDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for the activity
        setContentView(R.layout.activity_locations_list)

        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Database and DAO
        val database = LocationDatabase.getDatabase(applicationContext)
        locationDao = database.locationDao()

        // Initialize Adapter with mutable list
        locationAdapter = LocationAdapter(mutableListOf(), { location ->
        }, { location ->
            // Handle deletion here
            deleteLocation(location)
        })
        recyclerView.adapter = locationAdapter

        // Observe the database for changes
        observeLocations()


        addItemLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Refresh the list of locations
                observeLocations()
            }
        }


        findViewById<Button>(R.id.addLocationButton).setOnClickListener {
            // opens the AddLocationActivity to add a new location
            val intent = Intent(this, AddLocationActivity::class.java)
            addItemLauncher.launch(intent)
        }
    }

    private fun observeLocations() {
        lifecycleScope.launch {
            try {
                locationDao.getAllLocations().collectLatest { locations ->
                    // Update adapter with new data
                    locationAdapter.updateData(locations)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Delete the location from the database
    private fun deleteLocation(location: Location) {
        lifecycleScope.launch {
            try {
                locationDao.delete(location) // Call DAO method to delete the location
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
