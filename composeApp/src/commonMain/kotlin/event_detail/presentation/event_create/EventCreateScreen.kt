package event_detail.presentation.event_create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import event_detail.presentation.event_create.component.EventCreateScreenComponent
import event_detail.presentation.event_create.component.EventCreateScreenEvent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_description
import grabit.composeapp.generated.resources.event_detail_screen__featured_image
import grabit.composeapp.generated.resources.event_name
import grabit.composeapp.generated.resources.placeholder
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.LightGrey
import ui.theme.OnLime
import ui.theme.Shapes
import ui.theme.Typography


@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun EventCreateScreen(component: EventCreateScreenComponent) {
    val eventName by component.eventName.subscribeAsState()
    val description by component.description.subscribeAsState()


    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val menuItems = listOf("Item 1", "Item 2", "Item 3")
    var expanded by remember { mutableStateOf(true) }
    var selectedIndex by remember { mutableStateOf(-1) }

    Box(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
            .padding(bottom = 112.dp).background(
                Color.White
            ),
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Image(
                modifier = Modifier.fillMaxWidth().clip(Shapes.large),
                contentScale = ContentScale.Fit,
                painter = painterResource(Res.drawable.placeholder),
                contentDescription = stringResource(Res.string.event_detail_screen__featured_image),
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                textStyle = Typography.h2,
                value = eventName,
                onValueChange = { component.onEvent(EventCreateScreenEvent.ChangeEventName(it)) },
                label = { Text(stringResource(Res.string.event_name)) },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = LightGrey,
                    cursorColor = OnLime,
                    focusedLabelColor = OnLime,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                shape = Shapes.small
                )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                textStyle = Typography.body1,

                value = description,
                onValueChange = { component.onEvent(EventCreateScreenEvent.ChangeEventDescription(it)) },
                placeholder = { Text(stringResource(Res.string.event_description)) },
                minLines = 5,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = LightGrey,
                    cursorColor = OnLime,
                    focusedLabelColor = OnLime,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),

                shape = Shapes.small

                )

            Box(modifier = Modifier.clickable(onClick = {expanded = true})) {
                TextField(
                    modifier = Modifier.onFocusChanged { expanded = it.hasFocus },
                    value = if (selectedIndex >= 0) menuItems[selectedIndex] else "",
                    onValueChange = {},
                    readOnly = true
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    properties = PopupProperties(focusable = false)
                ) {
                    menuItems.forEachIndexed { index, title ->
                        DropdownMenuItem(onClick = {
                            selectedIndex = index
                            expanded = false
                            focusManager.clearFocus()
                        }) {
                            Text(text = title)
                        }
                    }
                }
            }
        }
    }
}