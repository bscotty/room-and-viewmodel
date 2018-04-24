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
    var date: String? = null

    @Ignore
    constructor(title: String) : this() {
        this.title = title
        generateId()
    }

    private fun generateId() {
        this.id = IdGenerator.gen()
    }
}