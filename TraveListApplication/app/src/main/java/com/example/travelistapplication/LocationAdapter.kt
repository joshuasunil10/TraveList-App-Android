package com.example.travelistapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog

class LocationAdapter(
    // Mutable list to hold the locations
    private val items: MutableList<Location>,
    private val onItemClick: (Location) -> Unit,
    private val onItemDelete: (Location) -> Unit
) : RecyclerView.Adapter<LocationAdapter.ItemViewHolder>() {

    // Displays the location name, country name, and priority in the RecyclerView
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryNameTextView: TextView = itemView.findViewById(R.id.countryNameTextView)
        val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        val priorityTextView: TextView = itemView.findViewById(R.id.PriorityTextView)
    }

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.location_view, parent, false)
        return ItemViewHolder(itemView)
    }

    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]

        // Set the text of the TextViews
        holder.itemNameTextView.text = currentItem.locationName
        holder.countryNameTextView.text = currentItem.countryName
        holder.priorityTextView.text = currentItem.priority.toString()

        // Handling opening each location's details when clicked
        holder.itemView.setOnClickListener {
            // Trigger the onItemClick callback if needed
            onItemClick(currentItem)

            // Create the Intent and include the ID as an extra
            val intent = Intent(holder.itemView.context, DetailLocationActivity::class.java)
            intent.putExtra("LOCATION_ID", currentItem.id)

            // Start the DetailLocation activity
            holder.itemView.context.startActivity(intent)
        }


        // Long press to delete
        holder.itemView.setOnLongClickListener {
            showDeleteDialog(holder.itemView.context, currentItem)
            true // Return true to indicate the event was handled
        }
    }

    override fun getItemCount() = items.size

    // Update the data in the adapter
    fun updateData(newItems: List<Location>) {
        items.clear()
        items.addAll(newItems)
        // Notify the adapter that the data has changed
        notifyDataSetChanged()
    }


    // Function to show the delete dialog
    private fun showDeleteDialog(context: Context, location: Location) {
        // Create an AlertDialog for delete confirmation
        val dialog = AlertDialog.Builder(context)
            .setTitle("Delete Location")
            .setMessage("Are you sure you want to delete this location entry?")
            .setPositiveButton("Yes") { _, _ ->
                onItemDelete(location) // Trigger delete action
            }
            .setNegativeButton("No", null)
            .create()

        dialog.show()
    }
}
