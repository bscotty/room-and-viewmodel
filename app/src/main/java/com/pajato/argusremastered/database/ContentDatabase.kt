package com.pajato.argusremastered.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.pajato.argusremastered.model.Content

@Database(entities = [Content::class], version = 4)
abstract class ContentDatabase : RoomDatabase() {
    abstract fun contentDao(): ContentDao

    companion object {
        private var sInstance: ContentDatabase? = null

        fun getDatabaseInstance(context: Context): ContentDatabase {
            if (sInstance == null) {
                sInstance = create(context)
            }
            return sInstance!!
        }

        private fun create(context: Context): ContentDatabase {
            val builder: RoomDatabase.Builder<ContentDatabase> = Room.databaseBuilder(context.applicationContext,
                    ContentDatabase::class.java,
                    DatabaseEntry.TABLE_NAME)
                    .fallbackToDestructiveMigration()
            return builder.build()
        }
    }
}