package com.since.todo.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.since.todo.data.database.modal.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(note: Note) : Long

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM note WHERE id LIKE(:id)")
    suspend fun getData(id:Int):Note?

    @Query("SELECT * FROM note ORDER BY time DESC")
    fun getAllData() : Flow<List<Note>>
}