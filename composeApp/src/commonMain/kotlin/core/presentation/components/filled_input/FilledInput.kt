package core.presentation.components.filled_input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ui.theme.LightGrey
import ui.theme.SecondaryText
import ui.theme.Shapes
import ui.theme.Typography

@Composable
fun FilledInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorText: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val isError = errorText != ""
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = true,
            shape = Shapes.medium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = LightGrey,
                focusedBorderColor = SecondaryText,
                cursorColor = SecondaryText,
                focusedLabelColor = SecondaryText,
                unfocusedBorderColor = Color.Transparent,
                unfocusedLabelColor = SecondaryText,
                errorBorderColor = Color.Red
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