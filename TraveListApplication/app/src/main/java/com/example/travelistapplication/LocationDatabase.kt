package com.example.travelistapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Location::class], version = 2, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {

    // Abstract function to get the LocationDao
    abstract fun locationDao(): LocationDao

    // Companion object to hold the singleton instance of LocationDatabase
    companion object {

        @Volatile
        private var INSTANCE: LocationDatabase? = null

        // Function to get a singleton instance of LocationDatabase
        fun getDatabase(context: Context): LocationDatabase {
            // If the instance is not null, return it or create a new instance
            return INSTANCE ?: synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationDatabase::class.java,
                    "location_database"
                )
                    // This will reset the database on version change
                    .fallbackToDestructiveMigration()
                    .build() // Build the Room database
                // Assign the instance to the tempInstance
                INSTANCE = tempInstance
                tempInstance // Return the new instance
            }
        }
    }
}
