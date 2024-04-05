package event_detail.presentation.event_create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import core.presentation.components.button_primary.ButtonPrimary
import event_detail.presentation.event_create.component.EventCreateScreenComponent
import event_detail.presentation.event_create.component.EventCreateScreenEvent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_description
import grabit.composeapp.generated.resources.event_detail_screen__end_harvest
import grabit.composeapp.generated.resources.event_detail_screen__end_harvest_notice
import grabit.composeapp.generated.resources.event_detail_screen__featured_image
import grabit.composeapp.generated.resources.event_name
import grabit.composeapp.generated.resources.placeholder
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.domain.ColorVariation
import ui.theme.LightGrey
import ui.theme.LightOnLime
import ui.theme.Shapes
import ui.theme.Typography


@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun EventCreateScreen(component: EventCreateScreenComponent) {
    val eventName by component.eventName.subscribeAsState()
    val description by component.description.subscribeAsState()

    val scope = rememberCoroutineScope()


    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val menuItems = listOf("Item 1", "Item 2", "Item 3")
    var expanded by remember { mutableStateOf(true) }
    var selectedIndex by remember { mutableStateOf(-1) }

    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }


    val singleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let {
                // Process the selected images' ByteArrays.
                println(it)
                imageBitmap.value = it.toImageBitmap()

            }
        }
    )


    Column(
        modifier = Modifier.fillMaxSize().navigationBarsPadding().padding(24.dp),
    ) {


        ButtonPrimary(
            ColorVariation.ORANGE,
            onClick = {
                singleImagePicker.launch()
            },
            text = "Pick Single Image"
        )
        imageBitmap.value?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = "null",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(Shapes.medium) // Use appropriate shape
                    .background(LightGrey)
                    .padding(16.dp),
                contentScale = ContentScale.Crop // Adjust the scaling to fit your needs
            )
        }

        Box(Modifier.fillMaxWidth()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ButtonPrimary(type = ColorVariation.CHERRY,
                    text = stringResource(Res.string.event_detail_screen__end_harvest),
                    onClick = {})
                Text(
                    text = stringResource(Res.string.event_detail_screen__end_harvest_notice),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
    }
}