package com.since.todo.presentation.notification.state_action

sealed interface NotificationAction {

    data class NotificationInfo(
        val rational: Boolean,
        val permission: Boolean
    ): NotificationAction


    data object DismissDialog : NotificationAction


}