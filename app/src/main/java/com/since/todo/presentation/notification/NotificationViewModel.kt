package com.since.todo.presentation.notification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.since.todo.presentation.notification.state_action.NotificationAction
import com.since.todo.presentation.notification.state_action.NotificationState

class NotificationViewModel : ViewModel() {

    var state by mutableStateOf(NotificationState())
        private set


    fun onAction(action: NotificationAction){
        when(action){
            is NotificationAction.NotificationInfo -> {
                state=state.copy(
                    rational = action.rational,
                    permission = action.permission,
                    dialog = !action.rational && !action.permission
                )
            }
            NotificationAction.DismissDialog -> {
                if(state.permission){
                    state=state.copy(
                        dialog = false
                    )
                }
            }

        }
    }

}