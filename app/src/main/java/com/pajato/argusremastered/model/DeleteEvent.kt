package com.pajato.argusremastered.model

import com.pajato.argusremastered.model.Content

class DeleteEvent(private val content: Content) : Event {
    override fun getData(): Content {
        return content
    }
}