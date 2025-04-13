package com.example.travelistapplication

// Acts as an Opening Activity for the application, entry into the app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getStarted = findViewById<Button>(R.id.getStarted)

        // Set click listener for the button
        getStarted.setOnClickListener {
            val intent = Intent(this, LocationList::class.java)
            startActivity(intent)

        }
    }
}
