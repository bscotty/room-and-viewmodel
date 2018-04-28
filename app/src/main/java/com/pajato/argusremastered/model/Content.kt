package com.pajato.argusremastered.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.pajato.argusremastered.util.IdGenerator

@Entity
class Content() {
    @PrimaryKey @NonNull
    var id: String? = null
    var title: String? = null
    var network: String? = null
    var date: String? = null
    var location: String? = null

    @Ignore
    constructor(title: String, network: String) : this() {
        this.title = title
        this.network = network
        generateId()
    }

    private fun generateId() {
        this.id = IdGenerator.gen()
    }
}