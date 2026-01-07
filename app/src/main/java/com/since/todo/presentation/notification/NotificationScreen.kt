package com.since.todo.presentation.notification

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.since.todo.R
import com.since.todo.presentation.notification.state_action.NotificationAction
import com.since.todo.presentation.notification.state_action.NotificationState
import com.since.todo.presentation.notification.utility.notificationPermission
import com.since.todo.presentation.notification.utility.notificationRational
import com.since.todo.presentation.notification.utility.openSetting

@Composable
fun NotificationScreen(
    state: NotificationState,
    action: (NotificationAction) -> Unit
) {

    val context = LocalContext.current
    val componentActivity = context as ComponentActivity

    val launchPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { perms->
        action(NotificationAction.NotificationInfo(
            rational = componentActivity.notificationRational(),
            permission = perms
        ))
    }

    LaunchedEffect(true) {
        if(!context.notificationPermission()){
            launchPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    if(state.rational){
        NotificationDialog(
            rational = componentActivity.notificationRational(),
            onPermissionLaunch = {
                launchPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            onSettingOpen = {}
        )
    }

    if(state.dialog){
        NotificationDialog(
            rational = componentActivity.notificationRational(),
            onPermissionLaunch = {},
            onSettingOpen = {
                context.openSetting()
            }
        )
    }


    val lifeCycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifeCycleOwner) {
        val lifeCycleEvent = LifecycleEventObserver{_,event ->
            if(Lifecycle.Event.ON_START == event){
                if(!context.notificationPermission()){
                    action(NotificationAction.NotificationInfo(
                        rational = componentActivity.notificationRational(),
                        permission = context.notificationPermission()
                    ))
                }

                if(context.notificationPermission()){
                    action(NotificationAction.DismissDialog)
                }
            }
        }

        lifeCycleOwner.lifecycle.addObserver(lifeCycleEvent)

        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(lifeCycleEvent)
        }
    }
}

@Composable
fun NotificationDialog(
    rational: Boolean,
    onPermissionLaunch : () -> Unit,
    onSettingOpen : () -> Unit
) {

    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            Button(
                onClick = {
                    if(rational) onPermissionLaunch() else onSettingOpen()
                },
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Text(text = "Ok")
            }
        },
        title = {
            Text(
                text = "Enable notification",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif
                )
            )
        },
        text = {
            Text(text = if(rational) stringResource(R.string.rational) else stringResource(R.string.setting_open),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }, shape = RoundedCornerShape(8.dp)

    )

}