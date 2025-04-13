package com.example.travelistapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert
    suspend fun insert(item: Location)

    @Update
    suspend fun update(item: Location)

    @Delete
    suspend fun delete(item: Location)  // Deletes a specific item

    @Query("SELECT * FROM location")
    fun getAllLocations(): Flow<List<Location>>


    @Query("SELECT * FROM Location WHERE id = :id")
    suspend fun getItemById(id: Int): Location?  // Retrieves an item by its ID

    @Query("SELECT * FROM Location WHERE CountryName = :name")
    suspend fun getItemsByName(name: String): List<Location>  // Retrieves items by name



}
