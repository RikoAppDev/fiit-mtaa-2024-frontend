package event.presentation.create_update.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import ui.theme.Shapes
import ui.theme.Typography

@Composable
fun SearchBarPlaceholder(
    query: String,
    onActiveChange: (Boolean) -> Unit,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = Shapes.medium,
) {
    Box(modifier = Modifier.clickable(
        indication = null,
        interactionSource = MutableInteractionSource()
    ) {
        onActiveChange(true)
    }) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            onValueChange = { },
            enabled = false,
            label = label,
            singleLine = true,
            shape = shape,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colors.secondary,
                disabledTextColor = MaterialTheme.colors.secondary,
                disabledPlaceholderColor = MaterialTheme.colors.secondary,
                disabledLabelColor = MaterialTheme.colors.onSurface,
                disabledLeadingIconColor = MaterialTheme.colors.secondary,
                disabledTrailingIconColor = MaterialTheme.colors.secondary,
                backgroundColor = MaterialTheme.colors.surface,
                focusedBorderColor = MaterialTheme.colors.surface,
                cursorColor = MaterialTheme.colors.surface,
                focusedLabelColor = MaterialTheme.colors.onSurface,
                unfocusedBorderColor = Color.Transparent,
                unfocusedLabelColor = MaterialTheme.colors.onSurface,
                errorBorderColor = Color.Red,
                disabledBorderColor = Color.Transparent
            ),
            textStyle = Typography.body1
        )
    }
}