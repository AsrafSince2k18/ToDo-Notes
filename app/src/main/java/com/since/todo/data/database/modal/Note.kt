package com.since.todo.data.database.modal

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "note")
data class Note(

    val title:String,
    val content:String,
    val oneTimeAlarm: Boolean,
    val repeatTimeAlarm : Boolean,
    val time:Long,

    @PrimaryKey(autoGenerate = true)
    val id:Int=0

)
