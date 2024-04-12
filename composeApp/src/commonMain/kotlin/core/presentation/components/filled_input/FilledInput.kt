package core.presentation.components.filled_input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ui.theme.Shapes
import ui.theme.Typography

@Composable
fun FilledInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    modifierOutlinedField: Modifier = Modifier,
    modifierColumn: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorText: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val isError = errorText != ""
    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = modifierColumn) {
        OutlinedTextField(
            modifier = modifierOutlinedField.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            label = { Text(label) },
            singleLine = singleLine,
            shape = Shapes.medium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colors.secondary,
                backgroundColor = MaterialTheme.colors.surface,
                focusedBorderColor = MaterialTheme.colors.surface,
                cursorColor = MaterialTheme.colors.surface,
                focusedLabelColor = MaterialTheme.colors.onSurface,
                unfocusedBorderColor = Color.Transparent,
                unfocusedLabelColor = MaterialTheme.colors.onSurface,
                errorBorderColor = Color.Red,
                disabledBorderColor = Color.Transparent
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            textStyle = Typography.body1,
            isError = isError,
        )
        if (isError) {
            Box(Modifier.padding(start = 16.dp)) {
                Text(
                    text = errorText,
                    style = Typography.body2,
                    color = Color.Red
                )
            }
        }
    }
}