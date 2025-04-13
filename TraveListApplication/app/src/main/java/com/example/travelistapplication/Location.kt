package com.example.travelistapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

// ROOM database entity class
// Location data class with the following attributes
@Entity(tableName = "location")
data class Location(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val locationName: String,
    val countryName: String,
    val priority: String,
    val description: String,
) {

}
