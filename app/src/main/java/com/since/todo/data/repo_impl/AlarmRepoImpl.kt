package com.since.todo.data.repo_impl

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.since.todo.data.database.modal.Note
import com.since.todo.domain.repo.AlarmRepo
import com.since.todo.presentation.reciver.AlarmReceiver
import com.since.todo.presentation.reciver.Utility

class AlarmRepoImpl(
    private val context: Context
) : AlarmRepo {

    val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override suspend fun setAlarm(note: Note) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action= Utility.START_SERVICE
            putExtra("key", Gson().toJson(note))
        }

        val pendingIntent= PendingIntent.getBroadcast(
            context,
            note.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManger.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                note.time,
                pendingIntent
            )
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    override suspend fun cancelAlarm(note: Note) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action= Utility.START_SERVICE
            putExtra("key", Gson().toJson(note))
        }

        val pendingIntent= PendingIntent.getBroadcast(
            context,
            note.id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManger.cancel(pendingIntent)
            pendingIntent.cancel()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}