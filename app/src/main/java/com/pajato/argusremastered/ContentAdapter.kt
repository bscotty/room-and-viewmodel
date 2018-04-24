package com.pajato.argusremastered

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pajato.argusremastered.model.Content
import com.pajato.argusremastered.model.DeleteEvent
import com.pajato.argusremastered.model.UpdateEvent
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
        holder.layout.deleteButton.setOnClickListener {
            RxBus.send(DeleteEvent(items[holder.adapterPosition]))
        }
        holder.layout.setOnClickListener {
            RxBus.send(UpdateEvent(items[holder.adapterPosition]))
        }
        holder.layout.dateText.text = items[holder.adapterPosition].date
    }

    class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout)
}