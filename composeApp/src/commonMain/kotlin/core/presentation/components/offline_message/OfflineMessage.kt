package core.presentation.components.offline_message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.offline_message_text
import grabit.composeapp.generated.resources.offline_message_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.theme.Shapes

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OfflineMessage() {
    Column(
        Modifier.fillMaxWidth().background(MaterialTheme.colors.error, shape = Shapes.medium)
            .padding(12.dp)
    ) {
        Text(
            stringResource(Res.string.offline_message_title),
            style = MaterialTheme.typography.h2,
            color = Color.White
        )
        Text(
            stringResource(Res.string.offline_message_text),
            style = MaterialTheme.typography.body1,
            color = Color.White
        )
    }

}