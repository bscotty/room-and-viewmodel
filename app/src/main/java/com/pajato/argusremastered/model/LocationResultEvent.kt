package com.pajato.argusremastered.model

import android.location.Location

class LocationResultEvent(private val content: Content, private val location: Location) : Event {
    override fun getData(): Content {
        return content
    }

    fun getLocation(): Location {
        return location
    }
}