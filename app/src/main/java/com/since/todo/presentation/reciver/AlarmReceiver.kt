package com.since.todo.presentation.reciver

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.since.todo.MyApplication.Companion.CHANNEL_ID
import com.since.todo.R
import com.since.todo.data.database.modal.Note
import com.since.todo.domain.repo.AlarmRepo
import com.since.todo.domain.repo.NoteRepo
import com.since.todo.presentation.reciver.Utility.STOP_SERVICE
import com.since.todo.presentation.reciver.Utility.START_SERVICE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmRepo: AlarmRepo

    @Inject
    lateinit var noteRepo: NoteRepo
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    override fun onReceive(context: Context, intent: Intent?) {

        when(intent?.action){
            START_SERVICE -> {
                showNotification(context=context,intent=intent)
            }
            STOP_SERVICE -> {
                val getKey = intent.getStringExtra("keys")
                    ?: throw IllegalArgumentException("Cannel notification error")

                val note = Gson().fromJson(getKey,Note::class.java)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                try {
                    scope.launch {
                        scope.launch {
                            alarmRepo.cancelAlarm(note=note)
                        }.join()

                        scope.launch {
                            if(note!=null) {
                                val noteo = note.copy(
                                    repeatTimeAlarm = false
                                )
                                noteRepo.insertOrUpdate(note=noteo)
                            }
                        }

                    }

                    notificationManager.cancel(note.id)
                }catch (e: Exception){
                    e.printStackTrace()
                }finally {
                    goAsync().finish()
                }

            }
        }

    }


    private fun showNotification(context: Context,
                                 intent: Intent){

        val getKey = intent.getStringExtra("key")
            ?: throw IllegalArgumentException("Key invalidate")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val note = Gson().fromJson(getKey, Note::class.java)

        val cancelIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = STOP_SERVICE
            putExtra("keys", Gson().toJson(note))
        }
        val pendingIntent = PendingIntent.getBroadcast(context,
            note.id,
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


        val notification = NotificationCompat.Builder(context,CHANNEL_ID)
            .setContentTitle(note.title)
            .setContentText(note.content)
            .setSmallIcon(R.drawable.logo)
            .addAction(R.drawable.logo,"Cancel",pendingIntent)
            .build()
        notificationManager.notify(note.id,notification)

        if(note.repeatTimeAlarm){

            val interval = 30L *1000L
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                action=START_SERVICE
                putExtra("key", Gson().toJson(note))
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                note.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis()+interval,
                pendingIntent
            )

        }

    }

}