package com.since.todo.presentation.state_action

sealed interface NoteAction {

    data class Title(val title:String): NoteAction
    data class Content(val content:String) : NoteAction

    data class OneTime(val alarm: Boolean) : NoteAction
    data class RepeatTime(val alarm: Boolean) : NoteAction
    data class Time(val time:Long) : NoteAction

    data class GetData(val id:Int) : NoteAction
    data class DeleteData(val id:Int) : NoteAction
    data object SaveData : NoteAction
    data object ClearState : NoteAction




}