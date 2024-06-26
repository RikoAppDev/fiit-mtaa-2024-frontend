package core.presentation.components.snackbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ui.theme.DarkApple
import ui.theme.LightApple

class SnackbarVisualWithError(
    val snackbarData: SnackbarData,
    val isError: Boolean
) {
    val actionLabel: String
        get() = if (isError) "😥" else "👌"
}

@Composable
fun CustomSnackbar(
    data: SnackbarVisualWithError,
    modifier: Modifier = Modifier,
    actionOnNewLine: Boolean = false,
    shape: Shape = MaterialTheme.shapes.small,
) {
    val isError = data.isError
    val buttonColor = if (isError) {
        ButtonDefaults.textButtonColors(
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.onBackground
        )
    } else {
        ButtonDefaults.textButtonColors(
            backgroundColor = DarkApple,
            contentColor = MaterialTheme.colors.onBackground
        )
    }

    Snackbar(
        modifier = modifier.padding(12.dp),
        action = {
            TextButton(
                onClick = {
                    data.snackbarData.dismiss()
                },
                colors = buttonColor
            ) { Text(data.actionLabel) }
        },
        actionOnNewLine = actionOnNewLine,
        shape = shape,
        backgroundColor = if (isError) {
            MaterialTheme.colors.error
        } else {
            LightApple
        },
        contentColor = Color.Black,
        elevation = 0.dp
    ) {
        Text(
            text = data.snackbarData.message,
            style = MaterialTheme.typography.body1,
            color = Color.Black
        )
    }
}