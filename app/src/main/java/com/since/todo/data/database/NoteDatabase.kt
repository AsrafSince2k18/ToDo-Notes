package com.since.todo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.since.todo.data.database.modal.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao() : NoteDao

}