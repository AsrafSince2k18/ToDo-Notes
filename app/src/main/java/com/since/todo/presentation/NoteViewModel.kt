package com.since.todo.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.since.todo.data.database.modal.Note
import com.since.todo.domain.repo.AlarmRepo
import com.since.todo.domain.repo.NoteRepo
import com.since.todo.presentation.state_action.NoteAction
import com.since.todo.presentation.state_action.NoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
    private val alarmRepo: AlarmRepo
) : ViewModel() {

    val getAllData = noteRepo.getAllNote()
        .stateIn(viewModelScope, SharingStarted.Eagerly,emptyList())

    var state by mutableStateOf(NoteState())
        private set


    fun onAction(action: NoteAction) {
        when (action) {
            is NoteAction.Content -> {
                state = state.copy(
                    content = action.content
                )
            }

            is NoteAction.OneTime -> {
                state = state.copy(
                    oneTimeAlarm = action.alarm
                )
            }

            is NoteAction.RepeatTime -> {
                state = state.copy(
                    repeatTimeAlarm = action.alarm
                )
            }

            is NoteAction.Time -> {
                state = state.copy(
                    time = action.time
                )
            }

            is NoteAction.Title -> {
                state = state.copy(
                    title = action.title
                )
            }

            is NoteAction.DeleteData -> {
                deleteData(id=action.id)
            }

            NoteAction.SaveData -> {
                saveOrUpdate()
            }

            is NoteAction.GetData -> {
                getData(id=action.id)
            }

            NoteAction.ClearState -> {
                state=state.copy(
                    title = "",
                    content = "",
                    oneTimeAlarm = false,
                    repeatTimeAlarm = false,
                    note = null,
                    time= System.currentTimeMillis()
                )
            }
        }
    }


    private fun saveOrUpdate() {
        viewModelScope.launch {
            if (state.note != null) {
                val note = state.note!!
                    .copy(
                        title = state.title,
                        content = state.content,
                        oneTimeAlarm = state.oneTimeAlarm,
                        repeatTimeAlarm = state.repeatTimeAlarm,
                        time = state.time
                    )

                 val id=noteRepo.insertOrUpdate(note = note)
                val setAlarm = noteRepo.getData(id=id.toInt())
                if(state.oneTimeAlarm || state.repeatTimeAlarm){
                    alarmRepo.setAlarm(note=setAlarm!!)
                }
            } else {
                val note = Note(
                    title = state.title,
                    content = state.content,
                    oneTimeAlarm = state.oneTimeAlarm,
                    repeatTimeAlarm = state.repeatTimeAlarm,
                    time = state.time
                )
                val id = noteRepo.insertOrUpdate(note = note.copy(id=note.id))
                val setAlarm = noteRepo.getData(id=id.toInt())
                if(state.oneTimeAlarm || state.repeatTimeAlarm){
                    alarmRepo.setAlarm(note=setAlarm!!)
                }
            }
        }
    }


    private fun getData(id:Int) {
        viewModelScope.launch {
            val note= noteRepo.getData(id=id)
            if(note!=null){
                state=state.copy(
                    title = note.title,
                    content = note.content,
                    oneTimeAlarm = note.oneTimeAlarm,
                    repeatTimeAlarm = note.repeatTimeAlarm,
                    time = note.time,
                    note = note
                )
            }
        }
    }

    private fun deleteData(id:Int){
        viewModelScope.launch {
            val note = noteRepo.getData(id=id)
            if(note!=null){
                noteRepo.deleteData(note)
            }
        }
    }

}