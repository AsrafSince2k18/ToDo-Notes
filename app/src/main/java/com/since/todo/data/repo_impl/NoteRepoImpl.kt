package com.since.todo.data.repo_impl

import com.since.todo.data.database.NoteDao
import com.since.todo.data.database.modal.Note
import com.since.todo.domain.repo.NoteRepo
import kotlinx.coroutines.flow.Flow

class NoteRepoImpl(
    private val noteDao: NoteDao
) : NoteRepo {
    override suspend fun insertOrUpdate(note: Note): Long {
        return noteDao.insertOrUpdate(note)
    }

    override suspend fun deleteData(note: Note) {
        noteDao.delete(note)
    }

    override suspend fun getData(id: Int): Note? {
        return noteDao.getData(id)
    }

    override fun getAllNote(): Flow<List<Note>> {
        return noteDao.getAllData()
    }


}