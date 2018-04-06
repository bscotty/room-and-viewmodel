package com.pajato.argusremastered.database

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*
import com.pajato.argusremastered.model.Content

@Dao
interface ContentDao {
    @Query("SELECT * FROM ${DatabaseEntry.TABLE_NAME}")
    fun getAllContent(): LiveData<MutableList<Content>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertContent(content: Content)

    @Delete()
    fun deleteContent(content: Content)
}