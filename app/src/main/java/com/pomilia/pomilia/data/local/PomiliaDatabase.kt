package com.pomilia.pomilia.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PomiliaDatabase : RoomDatabase(){
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: PomiliaDatabase? = null

        fun getDatabase(context: Context): PomiliaDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PomiliaDatabase::class.java,
                    "pomilia_database"
                    ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}