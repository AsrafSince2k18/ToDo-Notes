package com.since.todo.presentation.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.since.todo.presentation.state_action.NoteAction

@Composable
fun ToDoTextField(
    title:String,
    placeholder:String,
    onValueChange : (String) -> Unit,
    imeAction: ImeAction
) {
    OutlinedTextField(
        value = title,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(text = placeholder,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.2f)
                )
            )
        }, keyboardOptions = KeyboardOptions(
            imeAction = imeAction
        ),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .fillMaxWidth()
    )

    Spacer(
        modifier=Modifier
            .height(4.dp)
    )
}