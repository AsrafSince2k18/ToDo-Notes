package com.since.todo.domain.repo

import com.since.todo.data.database.modal.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepo {

    suspend fun insertOrUpdate(note: Note) : Long

    suspend fun deleteData(note: Note)

    suspend fun getData(id:Int): Note?

    fun getAllNote() : Flow<List<Note>>

}