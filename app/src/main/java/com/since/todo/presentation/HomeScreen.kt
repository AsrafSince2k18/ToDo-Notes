package com.since.todo.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.since.todo.data.database.modal.Note
import com.since.todo.presentation.component.ToDoComponent
import com.since.todo.presentation.component.ToDoTextField
import com.since.todo.presentation.state_action.NoteAction
import com.since.todo.presentation.state_action.NoteState
import com.since.todo.ui.theme.ToDoTheme
import kotlinx.coroutines.launch
import java.util.Calendar


@Composable
fun HomeScreenRoot(
    viewModel: NoteViewModel = hiltViewModel()
) {

    HomeScreen(
        noteList = viewModel.getAllData.collectAsStateWithLifecycle().value,
        state = viewModel.state,
        action = viewModel::onAction
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    noteList: List<Note> = emptyList(),
    state: NoteState,
    action: (NoteAction) -> Unit
) {

    val bottomSheet = rememberModalBottomSheetState()
    val scope= rememberCoroutineScope()
    val clockSheet = rememberUseCaseState()

    ClockDialog(
        state = clockSheet,
        selection = ClockSelection.HoursMinutes{hrs,min->
            val calender = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY,hrs)
                set(Calendar.MINUTE,min)
            }
            action(NoteAction.Time(calender.timeInMillis))
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "ToDo",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Serif
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                        action(NoteAction.ClearState)

                    scope.launch {
                        bottomSheet.expand()
                    }

                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.EditNote,
                    contentDescription = null
                )
            }
        },
        bottomBar = {
            if (bottomSheet.isVisible){
                ModalBottomSheet(
                    sheetState = bottomSheet,
                    onDismissRequest = {
                        scope.launch {
                            bottomSheet.hide()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    tonalElevation = 4.dp,
                    content = {
                        Column(
                            modifier=Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ToDoTextField(
                                title = state.title,
                                placeholder = "Title",
                                onValueChange = {
                                    action(NoteAction.Title(it))
                                },
                                imeAction = ImeAction.Next
                            )

                            Spacer(
                                modifier=Modifier
                                    .height(4.dp)
                            )

                            ToDoTextField(
                                title = state.content,
                                placeholder = "Content",
                                onValueChange = {
                                    action(NoteAction.Content(it))
                                },
                                imeAction = ImeAction.Done
                            )

                            Spacer(
                                modifier=Modifier
                                    .height(4.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Text(text = "Scheduled One time",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontFamily = FontFamily.Serif
                                    )
                                )

                                Switch(
                                    checked = state.oneTimeAlarm,
                                    onCheckedChange = {
                                        action(NoteAction.OneTime(it))
                                    }
                                )

                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Text(text = "Scheduled Repeat time",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontFamily = FontFamily.Serif
                                    )
                                )

                                Switch(
                                    checked = state.repeatTimeAlarm,
                                    onCheckedChange = {
                                        action(NoteAction.RepeatTime(it))
                                    }
                                )

                            }


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Text(text = "Set time",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontFamily = FontFamily.Serif
                                    )
                                )

                                IconButton(
                                    onClick = {
                                        clockSheet.show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.AccessTime,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }



                            Spacer(
                                modifier=Modifier
                                    .height(4.dp)
                            )
                            Button(
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                                onClick = {
                                    action(NoteAction.SaveData)
                                    scope.launch {
                                        bottomSheet.hide()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .padding(horizontal = 6.dp, vertical = 6.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(text = if(state.note!=null) "Update" else "Save",
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }

                        }
                    }
                )
            }
        }
    ) {paddingValues ->
        if(noteList.isEmpty()){
            Box(
                modifier=Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ){
                Text(text = "Add new note")
            }
        }else{
            LazyColumn(
                modifier=Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(noteList){note->
                    ToDoComponent(
                        note = note,
                        onClick = {
                            action(NoteAction.GetData(note.id))
                            scope.launch {
                                bottomSheet.expand()
                            }
                        }, onDelete = {
                            action(NoteAction.DeleteData(note.id))
                        }
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun HomeScreenPreview() {
    ToDoTheme{
        HomeScreen(
            state = NoteState()
        ) { }
    }
}