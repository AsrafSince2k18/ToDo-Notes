package com.since.todo.domain.repo

import com.since.todo.data.database.modal.Note

interface AlarmRepo {

    suspend fun setAlarm(note: Note)

    suspend fun cancelAlarm(note: Note)

}