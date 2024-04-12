package event.presentation.create_update

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import core.data.helpers.event.printifyEventDateTime
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.filled_input.FilledInput
import event.domain.model.SalaryType
import event.presentation.create_update.component.EventCreateUpdateScreenComponent
import event.presentation.create_update.component.EventCreateUpdateScreenEvent
import event.presentation.create_update.composables.CustomDateTimePickerDialog
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.cancel
import grabit.composeapp.generated.resources.capacity
import grabit.composeapp.generated.resources.done
import grabit.composeapp.generated.resources.event_create_update_screen__about_harvest
import grabit.composeapp.generated.resources.event_create_update_screen__add_category
import grabit.composeapp.generated.resources.event_create_update_screen__categories
import grabit.composeapp.generated.resources.event_create_update_screen__create_harvest
import grabit.composeapp.generated.resources.event_create_update_screen__date
import grabit.composeapp.generated.resources.event_create_update_screen__date_picker
import grabit.composeapp.generated.resources.event_create_update_screen__event_information
import grabit.composeapp.generated.resources.event_create_update_screen__event_location
import grabit.composeapp.generated.resources.event_create_update_screen__input_capacity
import grabit.composeapp.generated.resources.event_create_update_screen__location_of_harvest
import grabit.composeapp.generated.resources.event_create_update_screen__pick_image
import grabit.composeapp.generated.resources.event_create_update_screen__provided
import grabit.composeapp.generated.resources.event_create_update_screen__required
import grabit.composeapp.generated.resources.event_create_update_screen__salary
import grabit.composeapp.generated.resources.event_create_update_screen__salary_amount
import grabit.composeapp.generated.resources.event_create_update_screen__salary_good_desc
import grabit.composeapp.generated.resources.event_create_update_screen__salary_good_amount
import grabit.composeapp.generated.resources.event_create_update_screen__salary_money_desc
import grabit.composeapp.generated.resources.event_create_update_screen__salary_title
import grabit.composeapp.generated.resources.event_create_update_screen__salary_unit
import grabit.composeapp.generated.resources.event_create_update_screen__time_picker
import grabit.composeapp.generated.resources.event_create_update_screen__title
import grabit.composeapp.generated.resources.image_placeholder
import grabit.composeapp.generated.resources.my_events_screen__create_harvest
import grabit.composeapp.generated.resources.sallary_type_goods
import grabit.composeapp.generated.resources.sallary_type_money
import grabit.composeapp.generated.resources.tooling
import grabit.composeapp.generated.resources.top_bar_navigation__back
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.domain.ColorVariation
import ui.theme.DarkOnOrange
import ui.theme.DarkOrange
import ui.theme.LightGrey
import ui.theme.LightOnOrange
import ui.theme.LightOrange
import ui.theme.Shapes

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EventCreateUpdateScreen(component: EventCreateUpdateScreenComponent) {
    val stateEventCreateUpdate by component.stateEventCreateUpdate.subscribeAsState()
    val stateEvent by component.stateEvent.subscribeAsState()

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val actualDate =
                    Clock.System.now().toLocalDateTime(TimeZone.UTC).date.toEpochDays()
                return Instant.fromEpochMilliseconds(utcTimeMillis)
                    .toLocalDateTime(TimeZone.UTC).date.toEpochDays() >= actualDate
            }

            override fun isSelectableYear(year: Int): Boolean {
                val actualYear = Clock.System.now().toLocalDateTime(TimeZone.UTC).year
                return year >= actualYear
            }
        }
    )
    var showDatePicker by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }

    var salaryType by remember { mutableStateOf(SalaryType.MONEY) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val menuItems = listOf("Item 1", "Item 2", "Item 3")
    var expanded by remember { mutableStateOf(true) }
    var selectedIndex by remember { mutableStateOf(-1) }

    val imageBitmap = remember { mutableStateOf<ByteArray?>(null) }


    val singleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let {
                // Process the selected images' ByteArrays.
                imageBitmap.value = it
            }
        }
    )

    val todayContentColor = if (isSystemInDarkTheme()) {
        DarkOrange
    } else {
        LightOnOrange
    }

    val selectedDayContentColor = if (isSystemInDarkTheme()) {
        DarkOnOrange
    } else {
        LightOnOrange
    }

    val selectedDayContainerColor = if (isSystemInDarkTheme()) {
        DarkOrange
    } else {
        LightOrange
    }

    val disabledDayContentColor = if (isSystemInDarkTheme()) {
        Color.DarkGray
    } else {
        Color.LightGray
    }

    val timePickerColor = if (isSystemInDarkTheme()) {
        DarkOrange
    } else {
        MaterialTheme.colors.primary
    }

    val timeContainer = if (isSystemInDarkTheme()) {
        Color.DarkGray
    } else {
        LightGrey
    }

    val dialContent = if (isSystemInDarkTheme()) {
        DarkOnOrange
    } else {
        MaterialTheme.colors.primaryVariant
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colors.surface,
                    titleContentColor = Color.Black,
                ),
                title = {
                    Text(
                        stringResource(Res.string.my_events_screen__create_harvest),
                        style = MaterialTheme.typography.h3,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colors.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        component.onEvent(EventCreateUpdateScreenEvent.OnBackButtonClick)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(Res.string.top_bar_navigation__back),
                            tint = LightOnOrange
                        )
                    }
                },
            )
        }, bottomBar = {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background).padding(24.dp)
            ) {
                /*Box(Modifier.fillMaxWidth()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ButtonPrimary(type = ColorVariation.CHERRY,
                            text = stringResource(Res.string.event_detail_screen__end_harvest),
                            onClick = {}
                        )
                        Text(
                            text = stringResource(Res.string.event_detail_screen__end_harvest_notice),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }*/

                ButtonPrimary(
                    ColorVariation.APPLE,
                    onClick = {
                        component.onEvent(EventCreateUpdateScreenEvent.UpdateImage(imageBitmap.value!!))
                    },
                    text = stringResource(Res.string.event_create_update_screen__create_harvest)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(42.dp),
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.event_create_update_screen__event_information),
                        style = MaterialTheme.typography.h2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        // Image
                        if (imageBitmap.value != null) {
                            Image(
                                bitmap = imageBitmap.value!!.toImageBitmap(),
                                contentDescription = stringResource(Res.string.event_create_update_screen__pick_image),
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .clip(Shapes.medium)
                                    .background(MaterialTheme.colors.surface, Shapes.medium)
                                    .clickable {
                                        singleImagePicker.launch()
                                    },
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .clip(Shapes.medium)
                                    .background(MaterialTheme.colors.surface, Shapes.medium)
                                    .clickable {
                                        singleImagePicker.launch()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.image_placeholder),
                                    contentDescription = stringResource(Res.string.event_create_update_screen__pick_image)
                                )
                            }
                        }
                        ButtonPrimary(
                            ColorVariation.ORANGE,
                            onClick = {
                                singleImagePicker.launch()
                            },
                            text = stringResource(Res.string.event_create_update_screen__pick_image)
                        )
                        // Info
                        FilledInput(
                            value = stateEvent.title,
                            onValueChange = {
                                component.onEvent(EventCreateUpdateScreenEvent.UpdateTitle(it))
                            },
                            label = stringResource(Res.string.event_create_update_screen__title),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                        )
                        FilledInput(
                            value = stateEvent.description,
                            onValueChange = {
                                component.onEvent(EventCreateUpdateScreenEvent.UpdateDescription(it))
                            },
                            label = stringResource(Res.string.event_create_update_screen__about_harvest),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = false,
                            modifierOutlinedField = Modifier.height(140.dp)
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(48.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {
                    // Capacity
                    Column {
                        Text(
                            text = stringResource(Res.string.capacity),
                            style = MaterialTheme.typography.h2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        FilledInput(
                            value = stateEvent.capacity,
                            onValueChange = {
                                component.onEvent(EventCreateUpdateScreenEvent.UpdateCapacity(it))
                            },
                            label = stringResource(Res.string.event_create_update_screen__input_capacity),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions {
                                focusManager.clearFocus()
                            }
                        )
                    }
                    // Date
                    Column {
                        Text(
                            text = stringResource(Res.string.event_create_update_screen__date),
                            style = MaterialTheme.typography.h2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (showDatePicker) {
                            DatePickerDialog(
                                onDismissRequest = {
                                    showDatePicker = false
                                },
                                confirmButton = {
                                    ButtonPrimary(
                                        type = ColorVariation.ORANGE,
                                        text = stringResource(Res.string.done),
                                        onClick = {
                                            if (datePickerState.selectedDateMillis != null) {
                                                component.onEvent(
                                                    EventCreateUpdateScreenEvent.UpdateDate(
                                                        Instant.fromEpochMilliseconds(
                                                            datePickerState.selectedDateMillis!!
                                                        ).toLocalDateTime(TimeZone.UTC).date
                                                    )
                                                )
                                            }
                                            showDatePicker = false
                                        },
                                        buttonModifier = Modifier.width(IntrinsicSize.Min)
                                    )
                                },
                                dismissButton = {
                                    ButtonPrimary(
                                        type = ColorVariation.CHERRY,
                                        text = stringResource(Res.string.cancel),
                                        onClick = {
                                            showDatePicker = false
                                        },
                                        buttonModifier = Modifier.width(IntrinsicSize.Min),
                                    )
                                },
                                shape = Shapes.medium,
                                colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colors.background)
                            ) {
                                DatePicker(
                                    state = datePickerState,
                                    colors = DatePickerDefaults.colors(
                                        containerColor = MaterialTheme.colors.background,
                                        titleContentColor = MaterialTheme.colors.onBackground,
                                        navigationContentColor = todayContentColor,
                                        headlineContentColor = todayContentColor,
                                        weekdayContentColor = todayContentColor,
                                        todayContentColor = todayContentColor,
                                        todayDateBorderColor = todayContentColor,
                                        dayContentColor = MaterialTheme.colors.onBackground,
                                        selectedDayContentColor = selectedDayContentColor,
                                        selectedDayContainerColor = selectedDayContainerColor,
                                        yearContentColor = MaterialTheme.colors.onBackground,
                                        currentYearContentColor = todayContentColor,
                                        selectedYearContainerColor = selectedDayContainerColor,
                                        selectedYearContentColor = selectedDayContentColor,
                                        dividerColor = MaterialTheme.colors.primary,
                                        disabledDayContentColor = disabledDayContentColor,
                                        disabledYearContentColor = disabledDayContentColor,
                                        dateTextFieldColors = TextFieldDefaults.colors(
                                            focusedLabelColor = todayContentColor,
                                            cursorColor = todayContentColor,
                                            focusedContainerColor = MaterialTheme.colors.background,
                                            unfocusedContainerColor = MaterialTheme.colors.background,
                                            focusedTextColor = MaterialTheme.colors.onBackground,
                                            unfocusedTextColor = MaterialTheme.colors.onBackground,
                                            focusedIndicatorColor = todayContentColor,
                                            selectionColors = TextSelectionColors(
                                                handleColor = todayContentColor,
                                                backgroundColor = disabledDayContentColor
                                            )
                                        ),
                                    ),
                                )
                            }
                        }
                        if (showTimePicker) {
                            CustomDateTimePickerDialog(
                                title = stringResource(Res.string.event_create_update_screen__time_picker),
                                onDismissRequest = {
                                    showTimePicker = false
                                },
                                confirmButton = {
                                    ButtonPrimary(
                                        type = ColorVariation.ORANGE,
                                        text = stringResource(Res.string.done),
                                        onClick = {
                                            component.onEvent(
                                                EventCreateUpdateScreenEvent.UpdateTime(
                                                    LocalTime(
                                                        timePickerState.hour,
                                                        timePickerState.minute
                                                    )
                                                )
                                            )
                                            showTimePicker = false
                                        },
                                        buttonModifier = Modifier.width(IntrinsicSize.Min)
                                    )
                                },
                                dismissButton = {
                                    ButtonPrimary(
                                        type = ColorVariation.CHERRY,
                                        text = stringResource(Res.string.cancel),
                                        onClick = {
                                            showTimePicker = false
                                        },
                                        buttonModifier = Modifier.width(IntrinsicSize.Min),
                                    )
                                }
                            ) {
                                TimePicker(
                                    state = timePickerState,
                                    colors = TimePickerDefaults.colors(
                                        clockDialColor = timePickerColor,
                                        selectorColor = dialContent,
                                        timeSelectorSelectedContentColor = dialContent,
                                        timeSelectorSelectedContainerColor = timePickerColor,
                                        timeSelectorUnselectedContainerColor = timeContainer,
                                        clockDialUnselectedContentColor = dialContent,
                                        clockDialSelectedContentColor = timePickerColor,
                                    ),
                                )
                            }
                        }
                        Text(text = printifyEventDateTime(stateEvent.date, stateEvent.time))
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            ButtonPrimary(
                                text = stringResource(Res.string.event_create_update_screen__date_picker),
                                type = ColorVariation.ORANGE,
                                onClick = {
                                    showDatePicker = true
                                },
                                buttonModifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            ButtonPrimary(
                                text = stringResource(Res.string.event_create_update_screen__time_picker),
                                type = ColorVariation.ORANGE,
                                onClick = {
                                    showTimePicker = true
                                },
                                buttonModifier = Modifier.weight(1f)
                            )
                        }
                    }
                    // Tools
                    Column {
                        Text(
                            text = stringResource(Res.string.tooling),
                            style = MaterialTheme.typography.h2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        FilledInput(
                            value = stateEvent.requiredTools,
                            onValueChange = {
                                component.onEvent(
                                    EventCreateUpdateScreenEvent.UpdateRequiredTools(it)
                                )
                            },
                            label = stringResource(Res.string.event_create_update_screen__required),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        FilledInput(
                            value = stateEvent.providedTools,
                            onValueChange = {
                                component.onEvent(
                                    EventCreateUpdateScreenEvent.UpdateProvidedTools(it)
                                )
                            },
                            label = stringResource(Res.string.event_create_update_screen__provided),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions {
                                focusManager.clearFocus()
                            }
                        )
                    }
                    // Location
                    Column {
                        Text(
                            text = stringResource(Res.string.event_create_update_screen__location_of_harvest),
                            style = MaterialTheme.typography.h2,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        SearchBar(
                            query = stateEvent.searchLocation,
                            onQueryChange = {
                                component.onEvent(
                                    EventCreateUpdateScreenEvent.UpdateLocation(it)
                                )
                            },
                            onSearch = {

                                focusManager.clearFocus()
                            },
                            active = false,
                            onActiveChange = {

                            },
                            modifier = Modifier,
                            placeholder = {
                                Text(text = stringResource(Res.string.event_create_update_screen__event_location))
                            },
                            leadingIcon = {
                                IconButton(onClick = {

                                    focusManager.clearFocus()
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            },
                            trailingIcon = {
                                if (stateEvent.searchLocation.isNotEmpty()) {
                                    IconButton(onClick = {
                                        component.onEvent(
                                            EventCreateUpdateScreenEvent.UpdateLocation(
                                                ""
                                            )
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                            shape = Shapes.medium,
                            colors = SearchBarDefaults.colors(
                                containerColor = MaterialTheme.colors.surface,
                                dividerColor = MaterialTheme.colors.primary,
                                inputFieldColors = TextFieldDefaults.colors(
                                    focusedLabelColor = todayContentColor,
                                    cursorColor = todayContentColor,
                                    focusedContainerColor = MaterialTheme.colors.background,
                                    unfocusedContainerColor = MaterialTheme.colors.background,
                                    focusedTextColor = MaterialTheme.colors.onBackground,
                                    unfocusedTextColor = MaterialTheme.colors.onBackground,
                                    focusedIndicatorColor = todayContentColor,
                                    selectionColors = TextSelectionColors(
                                        handleColor = todayContentColor,
                                        backgroundColor = disabledDayContentColor
                                    )
                                )
                            )
                        ) {

                        }
                    }
                    // Salary
                    Column {
                        Text(
                            text = stringResource(Res.string.event_create_update_screen__salary),
                            style = MaterialTheme.typography.h2
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.clip(Shapes.medium)) {
                            Box(
                                modifier = Modifier.weight(1f).clickable {
                                    salaryType = SalaryType.MONEY
                                }.background(
                                    color = if (salaryType == SalaryType.MONEY) {
                                        selectedDayContainerColor
                                    } else {
                                        MaterialTheme.colors.background
                                    }
                                ).padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (salaryType == SalaryType.MONEY) {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = null,
                                            tint = dialContent
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(Res.string.sallary_type_money),
                                        color = if (salaryType == SalaryType.MONEY) {
                                            dialContent
                                        } else {
                                            MaterialTheme.colors.onBackground
                                        }
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier.weight(1f).clickable {
                                    salaryType = SalaryType.GOODS
                                }.background(
                                    color = if (salaryType == SalaryType.GOODS) {
                                        selectedDayContainerColor
                                    } else {
                                        MaterialTheme.colors.background
                                    }
                                ).padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (salaryType == SalaryType.GOODS) {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = null,
                                            tint = dialContent
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(Res.string.sallary_type_goods),
                                        color = if (salaryType == SalaryType.GOODS) {
                                            dialContent
                                        } else {
                                            MaterialTheme.colors.onBackground
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (salaryType == SalaryType.MONEY) {
                            Column {
                                FilledInput(
                                    value = stateEvent.salaryAmount,
                                    onValueChange = {
                                        component.onEvent(
                                            EventCreateUpdateScreenEvent.UpdateSalaryAmount(it)
                                        )
                                    },
                                    label = stringResource(Res.string.event_create_update_screen__salary_amount),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions {
                                        focusManager.clearFocus()
                                    }
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(
                                        Res.string.event_create_update_screen__salary_money_desc,
                                        stateEvent.salaryAmount,
                                        "€"
                                    ),
                                    style = MaterialTheme.typography.caption,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        } else {
                            Column {
                                Row {
                                    FilledInput(
                                        value = stateEvent.salaryAmount,
                                        onValueChange = {
                                            component.onEvent(
                                                EventCreateUpdateScreenEvent.UpdateSalaryAmount(it)
                                            )
                                        },
                                        label = stringResource(Res.string.event_create_update_screen__salary_good_amount),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Decimal,
                                            imeAction = ImeAction.Next
                                        ),
                                        modifierColumn = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    FilledInput(
                                        value = stateEvent.salaryUnit,
                                        onValueChange = {
                                            component.onEvent(
                                                EventCreateUpdateScreenEvent.UpdateSalaryUnit(it)
                                            )
                                        },
                                        label = stringResource(Res.string.event_create_update_screen__salary_unit),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Next
                                        ),
                                        modifierColumn = Modifier.weight(1f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                FilledInput(
                                    value = stateEvent.salaryGoodTitle,
                                    onValueChange = {
                                        component.onEvent(
                                            EventCreateUpdateScreenEvent.UpdateSalaryGoodTitle(it)
                                        )
                                    },
                                    label = stringResource(Res.string.event_create_update_screen__salary_title),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions {
                                        focusManager.clearFocus()
                                    }
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(
                                        Res.string.event_create_update_screen__salary_good_desc,
                                        stateEvent.salaryGoodTitle,
                                        stateEvent.salaryAmount,
                                        stateEvent.salaryUnit
                                    ),
                                    style = MaterialTheme.typography.caption,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                    // Categories
                    Column {
                        Text(
                            text = stringResource(Res.string.event_create_update_screen__categories),
                            style = MaterialTheme.typography.h2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        SearchBar(
                            query = stateEvent.searchCategory,
                            onQueryChange = {
                                component.onEvent(
                                    EventCreateUpdateScreenEvent.UpdateSearchCategory(it)
                                )
                            },
                            onSearch = {

                                focusManager.clearFocus()
                            },
                            active = false,
                            onActiveChange = {

                            },
                            modifier = Modifier,
                            placeholder = {
                                Text(text = stringResource(Res.string.event_create_update_screen__add_category))
                            },
                            leadingIcon = {
                                IconButton(onClick = {

                                    focusManager.clearFocus()
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            },
                            trailingIcon = {
                                if (stateEvent.searchLocation.isNotEmpty()) {
                                    IconButton(onClick = {
                                        component.onEvent(
                                            EventCreateUpdateScreenEvent.UpdateLocation(
                                                ""
                                            )
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                            shape = Shapes.medium,
                            colors = SearchBarDefaults.colors(
                                containerColor = MaterialTheme.colors.surface,
                                dividerColor = MaterialTheme.colors.primary,
                                inputFieldColors = TextFieldDefaults.colors(
                                    focusedLabelColor = todayContentColor,
                                    cursorColor = todayContentColor,
                                    focusedContainerColor = MaterialTheme.colors.background,
                                    unfocusedContainerColor = MaterialTheme.colors.background,
                                    focusedTextColor = MaterialTheme.colors.onBackground,
                                    unfocusedTextColor = MaterialTheme.colors.onBackground,
                                    focusedIndicatorColor = todayContentColor,
                                    selectionColors = TextSelectionColors(
                                        handleColor = todayContentColor,
                                        backgroundColor = disabledDayContentColor
                                    )
                                )
                            )
                        ) {

                        }
                    }
                }
            }
        }
    }
}