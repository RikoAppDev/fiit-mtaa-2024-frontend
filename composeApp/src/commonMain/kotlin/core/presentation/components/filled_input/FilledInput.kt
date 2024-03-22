package core.presentation.components.filled_input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.theme.LightGrey
import ui.theme.SecondaryText
import ui.theme.Shapes
import ui.theme.Typography

@OptIn(ExperimentalResourceApi::class)
@Composable
fun FilledInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualTransformation:VisualTransformation = VisualTransformation.None,
    isValid:Boolean = true
) {
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
            unfocusedLabelColor = SecondaryText
        ),
        visualTransformation = visualTransformation,
        textStyle = Typography.body1
    )

}