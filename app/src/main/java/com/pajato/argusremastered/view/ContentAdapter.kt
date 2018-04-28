package com.pajato.argusremastered.view

import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pajato.argusremastered.R
import com.pajato.argusremastered.model.Content
import com.pajato.argusremastered.model.DeleteEvent
import com.pajato.argusremastered.model.WatchedEvent
import com.pajato.argusremastered.util.RxBus
import kotlinx.android.synthetic.main.content_layout.view.*

class ContentAdapter(var items: List<Content>) : RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.content_layout, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.layout.titleText.setText(items[position].title)
        holder.layout.networkText.setText(items[position].network)
        holder.layout.deleteButton.setOnClickListener {
            RxBus.send(DeleteEvent(items[holder.adapterPosition]))
        }
        holder.layout.dateButton.setOnClickListener {
            RxBus.send(WatchedEvent(items[holder.adapterPosition]))
        }
        // Populate the Date Text and ensure if
        val dateText = items[holder.adapterPosition].date
        holder.layout.dateText.text = dateText
        if (dateText != null && dateText != "") {
            holder.layout.dateButton.setColorFilter(Color.parseColor("#8BC34A"), PorterDuff.Mode.SRC_ATOP)
        }
        val locationText = items[holder.adapterPosition].location
        holder.layout.locationText.text = locationText
    }

    class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout)
}