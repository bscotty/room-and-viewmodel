package com.pajato.argusremastered.model

class WatchedEvent(private val content: Content) : Event {
    override fun getData(): Content {
        return content
    }
}