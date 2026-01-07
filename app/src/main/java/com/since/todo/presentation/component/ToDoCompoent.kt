package com.since.todo.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.since.todo.data.database.modal.Note
import com.since.todo.ui.theme.ToDoTheme
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun ToDoComponent(
    note: Note,
    onClick: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            onClick(note.id)
        }, elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 5.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f)
            ) {

                Text(
                    text = note.title,
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp
                    )
                )

                Spacer(
                    modifier = Modifier
                        .height(4.dp)
                )

                Text(
                    text = note.content,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp
                    )
                )

                    if (note.oneTimeAlarm) {
                        Text(
                            text = "Scheduled one time",
                            style = TextStyle(
                                fontSize = 12.sp
                            )
                        )
                    }

                    if (note.repeatTimeAlarm) {
                        Text(
                            text = "Scheduled repeat time",
                            style = TextStyle(
                                fontSize = 12.sp
                            )
                        )
                    }

                Spacer(
                    modifier = Modifier
                        .height(6.dp)
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.2f)
                )


                Text(
                    text = note.time.timeFormat(),
                    style = TextStyle(
                        textAlign = TextAlign.Right,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

            }

            IconButton(
                onClick = {
                    onDelete(note.id)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null
                )
            }

        }


    }

}


@Preview
@Composable
private fun NoteCompoentPreview() {
    ToDoTheme(

    ) {
        ToDoComponent(
            note = Note(
                title = "Asraf",
                content = "sa",
                oneTimeAlarm = false,
                repeatTimeAlarm = true,
                time = System.currentTimeMillis()
            ),
            onClick = {}
        ) { }
    }
}

private fun Long.timeFormat(): String {
    val sdf = SimpleDateFormat("hh:mm a")
    val date = Date(this)
    return sdf.format(date)
}