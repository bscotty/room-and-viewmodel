package com.pajato.argusremastered.model

class UpdateEvent(private val content: Content) : Event {
    override fun getData(): Content {
        return content
    }
}