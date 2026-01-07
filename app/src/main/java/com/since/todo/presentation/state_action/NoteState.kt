package com.since.todo.presentation.state_action

import com.since.todo.data.database.modal.Note

data class NoteState(

    val title:String="",
    val content:String= "",
    val oneTimeAlarm: Boolean=false,
    val repeatTimeAlarm: Boolean=false,
    val time:Long = System.currentTimeMillis(),
    val note: Note?=null
)
