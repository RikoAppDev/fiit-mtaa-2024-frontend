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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import core.data.helpers.event.printifyEventDateTime
import core.domain.event.SallaryType
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.category_chip.CategoryChip
import core.presentation.components.cicrular_progress.CustomCircularProgress
import core.presentation.components.event_categories.EventCategories
import core.presentation.components.event_image.EventImage
import core.presentation.components.filled_input.FilledInput
import core.presentation.components.event_image.ImagePlaceholder
import core.presentation.components.snackbar.CustomSnackbar
import core.presentation.components.snackbar.SnackbarVisualWithError
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import event.presentation.create_update.component.EventCreateUpdateScreenComponent
import event.presentation.create_update.component.EventCreateUpdateScreenEvent
import event.presentation.create_update.composables.CustomDateTimePickerDialog
import event.presentation.create_update.composables.SearchBarPlaceholder
import event.presentation.create_update.composables.CustomSearchBarDialog
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.cancel
import grabit.composeapp.generated.resources.capacity
import grabit.composeapp.generated.resources.done
import grabit.composeapp.generated.resources.event_create_update_screen__about_harvest
import grabit.composeapp.generated.resources.event_create_update_screen__add_category
import grabit.composeapp.generated.resources.event_create_update_screen__all_removed_categories
import grabit.composeapp.generated.resources.event_create_update_screen__categories
import grabit.composeapp.generated.resources.event_create_update_screen__create_harvest
import grabit.composeapp.generated.resources.event_create_update_screen__date
import grabit.composeapp.generated.resources.event_create_update_screen__date_picker
import grabit.composeapp.generated.resources.event_create_update_screen__event_information
import grabit.composeapp.generated.resources.event_create_update_screen__event_location
import grabit.composeapp.generated.resources.event_create_update_screen__event_location_selected
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
import grabit.composeapp.generated.resources.event_create_update_screen__update_harvest
import grabit.composeapp.generated.resources.location
import grabit.composeapp.generated.resources.my_events_screen__create_harvest
import grabit.composeapp.generated.resources.sallary_type_goods
import grabit.composeapp.generated.resources.sallary_type_money
import grabit.composeapp.generated.resources.tooling
import grabit.composeapp.generated.resources.top_bar_navigation__back
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
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
    val stateEventImage by component.stateEventImage.subscribeAsState()
    val stateEvent by component.stateEvent.subscribeAsState()
    val stateIsUpdate by component.stateIsUpdate.subscribeAsState()
    val searchCategory by component.searchCategory.collectAsState()
    val searchLocation by component.searchLocation.collectAsState()
    val isSearching by component.isSearching.subscribeAsState()
    val categories by component.categories.collectAsState()
    val places by component.locations.collectAsState()
    val stateCategorySize by component.stateCategoriesSize.subscribeAsState()

    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val controller: PermissionsController =
        remember(factory) { factory.createPermissionsController() }

    BindEffect(controller)

    LaunchedEffect(true) {
        component.getCategories()
    }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val isVisible = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val actualDate =
                    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toEpochDays()
                return Instant.fromEpochMilliseconds(utcTimeMillis)
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date.toEpochDays() >= actualDate
            }

            override fun isSelectableYear(year: Int): Boolean {
                val actualYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
                return year >= actualYear
            }
        }
    )
    var showDatePicker by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }

    var activeLocationSearch by remember { mutableStateOf(false) }
    var activeCategorySearch by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val imageBitmap = remember { mutableStateOf<ByteArray?>(null) }

    val singleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let {
                imageBitmap.value = it
            }

            if (imageBitmap.value != null) {
                component.onEvent(EventCreateUpdateScreenEvent.UpdateImage(imageBitmap.value!!))
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

    if (!isVisible.value && stateEventCreateUpdate.error != null) {
        coroutineScope.launch {
            isVisible.value = true
            val snackbarResult = snackbarHostState.showSnackbar(
                message = stateEventCreateUpdate.error!!,
                duration = SnackbarDuration.Short
            )

            when (snackbarResult) {
                SnackbarResult.Dismissed -> {
                    isVisible.value = false
                    component.onEvent(EventCreateUpdateScreenEvent.RemoveError)
                }

                SnackbarResult.ActionPerformed -> {
                    isVisible.value = false
                    component.onEvent(EventCreateUpdateScreenEvent.RemoveError)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, snackbar = {
                CustomSnackbar(
                    data = SnackbarVisualWithError(
                        snackbarData = it,
                        isError = stateEventCreateUpdate.error != "",
                    )
                )
            })
        },
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
                if (stateIsUpdate) {
                    ButtonPrimary(
                        ColorVariation.LEMON,
                        onClick = {
                            component.onEvent(EventCreateUpdateScreenEvent.OnUpdateEventButtonClick)
                        },
                        text = stringResource(Res.string.event_create_update_screen__update_harvest)
                    )
                } else {
                    ButtonPrimary(
                        ColorVariation.APPLE,
                        onClick = {
                            component.onEvent(EventCreateUpdateScreenEvent.OnCreateEventButtonClick)
                        },
                        text = stringResource(Res.string.event_create_update_screen__create_harvest)
                    )
                }
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
                        Box {
                            if (stateEventImage.isLoading) {
                                CircularProgressIndicator(
                                    Modifier.align(Alignment.Center).zIndex(10f)
                                )
                            }
                            if (imageBitmap.value != null) {
                                Image(
                                    bitmap = imageBitmap.value!!.toImageBitmap(),
                                    contentDescription = stringResource(Res.string.event_create_update_screen__pick_image),
                                    modifier = Modifier
                                        .height(192.dp)
                                        .fillMaxWidth()
                                        .clip(Shapes.medium)
                                        .background(MaterialTheme.colors.surface, Shapes.medium)
                                        .clickable {
                                            coroutineScope.launch {
                                                try {
                                                    controller.providePermission(Permission.GALLERY)
                                                    if (!stateEventImage.isLoading) {
                                                        singleImagePicker.launch()
                                                    }
                                                } catch (e: DeniedAlwaysException) {
                                                    println("Error: $e")
                                                } catch (e: DeniedException) {
                                                    println("Error: $e")
                                                }
                                            }
                                        },
                                    contentScale = ContentScale.Crop
                                )
                            } else if (stateIsUpdate && stateEvent.imageUrl != null) {
                                EventImage(stateEvent.imageUrl!!, modifier = Modifier.clickable {
                                    coroutineScope.launch {
                                        try {
                                            controller.providePermission(Permission.GALLERY)
                                            if (!stateEventImage.isLoading) {
                                                singleImagePicker.launch()
                                            }
                                        } catch (e: DeniedAlwaysException) {
                                            println("Error: $e")
                                        } catch (e: DeniedException) {
                                            println("Error: $e")
                                        }
                                    }
                                })
                            } else {
                                ImagePlaceholder(modifier = Modifier.clickable {
                                    coroutineScope.launch {
                                        try {
                                            controller.providePermission(Permission.GALLERY)
                                            if (!stateEventImage.isLoading) {
                                                singleImagePicker.launch()
                                            }
                                        } catch (e: DeniedAlwaysException) {
                                            println("Error: $e")
                                        } catch (e: DeniedException) {
                                            println("Error: $e")
                                        }
                                    }
                                })
                            }
                        }
                        ButtonPrimary(
                            ColorVariation.ORANGE,
                            onClick = {
                                if (!stateEventImage.isLoading) {
                                    coroutineScope.launch {
                                        try {
                                            controller.providePermission(Permission.GALLERY)
                                            if (!stateEventImage.isLoading) {
                                                singleImagePicker.launch()
                                            }
                                        } catch (e: DeniedAlwaysException) {
                                            println("Error: $e")
                                        } catch (e: DeniedException) {
                                            println("Error: $e")
                                        }
                                    }
                                }
                            },
                            text = stringResource(Res.string.event_create_update_screen__pick_image)
                        )
                        // Info
                        FilledInput(
                            enabled = !stateIsUpdate,
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
                                component.onEvent(
                                    EventCreateUpdateScreenEvent.UpdateDescription(
                                        it
                                    )
                                )
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
                                                        ).toLocalDateTime(TimeZone.currentSystemDefault()).date
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
                        SearchBarPlaceholder(
                            query = searchLocation,
                            onActiveChange = {
                                activeLocationSearch = it
                            },
                            label = {
                                if (searchLocation.isEmpty()) {
                                    Text(text = stringResource(Res.string.event_create_update_screen__event_location))
                                } else {
                                    Text(text = stringResource(Res.string.event_create_update_screen__event_location_selected))
                                }
                            },
                            leadingIcon = {
                                if (searchLocation.isEmpty()) {
                                    IconButton(onClick = {
                                        focusManager.clearFocus()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Search,
                                            contentDescription = null
                                        )
                                    }
                                } else {
                                    IconButton(onClick = {
                                        focusManager.clearFocus()
                                    }) {
                                        Icon(
                                            imageVector = vectorResource(Res.drawable.location),
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                            trailingIcon = {
                                if (searchLocation.isNotEmpty()) {
                                    IconButton(onClick = {
                                        component.onEvent(
                                            EventCreateUpdateScreenEvent.UpdateSearchLocation("")
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        )

                        if (activeLocationSearch) {
                            CustomSearchBarDialog(
                                query = searchLocation,
                                onQueryChange = {
                                    component.onEvent(
                                        EventCreateUpdateScreenEvent.UpdateSearchLocation(it)
                                    )
                                },
                                onSearch = {
                                    activeLocationSearch = false
                                    focusManager.clearFocus()
                                },
                                active = activeLocationSearch,
                                onActiveChange = {
                                    activeLocationSearch = it
                                },
                                placeholder = {
                                    Text(text = stringResource(Res.string.event_create_update_screen__event_location))
                                },
                                leadingIcon = {
                                    IconButton(onClick = {
                                        activeLocationSearch = false
                                        focusManager.clearFocus()
                                    }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                            contentDescription = null
                                        )
                                    }
                                },
                                trailingIcon = {
                                    if (searchLocation.isNotEmpty()) {
                                        IconButton(onClick = {
                                            component.onEvent(
                                                EventCreateUpdateScreenEvent.UpdateSearchLocation(
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
                                ),
                                onDismissRequest = {
                                    activeLocationSearch = false
                                }
                            ) {
                                if (isSearching) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CustomCircularProgress(size = 40.dp)
                                    }
                                } else {
                                    LazyColumn {
                                        items(places) {
                                            Box(
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(vertical = 1.dp, horizontal = 2.dp)
                                                    .clip(Shapes.large)
                                                    .clickable {
                                                        component.onEvent(
                                                            EventCreateUpdateScreenEvent.UpdateSearchLocation(
                                                                it.mainText
                                                            )
                                                        )
                                                        component.onEvent(
                                                            EventCreateUpdateScreenEvent.UpdateLocation(
                                                                it.placeId
                                                            )
                                                        )
                                                        activeLocationSearch = false
                                                    }
                                            ) {
                                                Column(modifier = Modifier.padding(16.dp)) {
                                                    Text(
                                                        text = it.mainText,
                                                        style = MaterialTheme.typography.h3,
                                                        color = MaterialTheme.colors.onBackground
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = it.secondaryText,
                                                        style = MaterialTheme.typography.body1,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // Sallary
                    Column {
                        Text(
                            text = stringResource(Res.string.event_create_update_screen__salary),
                            style = MaterialTheme.typography.h2
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.clip(Shapes.medium)) {
                            Box(
                                modifier = Modifier.weight(1f).clickable {
                                    component.onEvent(
                                        EventCreateUpdateScreenEvent.UpdateSalaryType(
                                            SallaryType.MONEY
                                        )
                                    )
                                }.background(
                                    color = if (stateEvent.salaryType == SallaryType.MONEY) {
                                        selectedDayContainerColor
                                    } else {
                                        MaterialTheme.colors.background
                                    }
                                ).padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (stateEvent.salaryType == SallaryType.MONEY) {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = null,
                                            tint = dialContent
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(Res.string.sallary_type_money),
                                        color = if (stateEvent.salaryType == SallaryType.MONEY) {
                                            dialContent
                                        } else {
                                            MaterialTheme.colors.onBackground
                                        }
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier.weight(1f).clickable {
                                    component.onEvent(
                                        EventCreateUpdateScreenEvent.UpdateSalaryType(
                                            SallaryType.GOODS
                                        )
                                    )
                                }.background(
                                    color = if (stateEvent.salaryType == SallaryType.GOODS) {
                                        selectedDayContainerColor
                                    } else {
                                        MaterialTheme.colors.background
                                    }
                                ).padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (stateEvent.salaryType == SallaryType.GOODS) {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = null,
                                            tint = dialContent
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(Res.string.sallary_type_goods),
                                        color = if (stateEvent.salaryType == SallaryType.GOODS) {
                                            dialContent
                                        } else {
                                            MaterialTheme.colors.onBackground
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (stateEvent.salaryType == SallaryType.MONEY) {
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
                                                EventCreateUpdateScreenEvent.UpdateSalaryAmount(
                                                    it
                                                )
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
                                            EventCreateUpdateScreenEvent.UpdateSalaryGoodTitle(
                                                it
                                            )
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
                        Spacer(modifier = Modifier.height(24.dp))
                        if (stateCategorySize > 0) {
                            EventCategories(
                                stateEvent.categoryList,
                                removable = true,
                                onCategoryClick = {
                                    component.onEvent(
                                        EventCreateUpdateScreenEvent.RemoveCategory(
                                            it
                                        )
                                    )
                                }
                            )
                        } else {
                            Text(
                                text = stringResource(Res.string.event_create_update_screen__all_removed_categories),
                                style = MaterialTheme.typography.body2
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        SearchBarPlaceholder(
                            query = "",
                            onActiveChange = {
                                activeCategorySearch = it
                            },
                            label = {
                                Text(text = stringResource(Res.string.event_create_update_screen__add_category))
                            },
                            leadingIcon = {
                                IconButton(onClick = {
                                    focusManager.clearFocus()
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Search,
                                        contentDescription = null
                                    )
                                }
                            }
                        )

                        if (activeCategorySearch) {
                            CustomSearchBarDialog(
                                query = searchCategory,
                                onQueryChange = {
                                    component.onEvent(
                                        EventCreateUpdateScreenEvent.UpdateSearchCategory(it)
                                    )
                                },
                                onSearch = {
                                    activeCategorySearch = false
                                    focusManager.clearFocus()
                                },
                                active = activeCategorySearch,
                                onActiveChange = {
                                    activeCategorySearch = it
                                },
                                placeholder = {
                                    Text(text = stringResource(Res.string.event_create_update_screen__add_category))
                                },
                                leadingIcon = {
                                    IconButton(onClick = {
                                        activeCategorySearch = false
                                        focusManager.clearFocus()
                                    }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                            contentDescription = null
                                        )
                                    }
                                },
                                trailingIcon = {
                                    if (searchCategory.isNotEmpty()) {
                                        IconButton(onClick = {
                                            component.onEvent(
                                                EventCreateUpdateScreenEvent.UpdateSearchCategory("")
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
                                ),
                                onDismissRequest = {
                                    activeCategorySearch = false
                                }
                            ) {
                                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                    items(categories) { category ->
                                        Box(
                                            modifier = Modifier.fillMaxWidth()
                                                .padding(vertical = 1.dp, horizontal = 2.dp)
                                                .clip(Shapes.large)
                                                .clickable {
                                                    component.onEvent(
                                                        EventCreateUpdateScreenEvent.AddCategory(
                                                            category
                                                        )
                                                    )
                                                    activeCategorySearch = false
                                                }
                                        ) {
                                            CategoryChip(
                                                text = "${category.icon} ${category.name}",
                                                color = category.colorVariant,
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(horizontal = 24.dp, vertical = 8.dp),
                                                style = MaterialTheme.typography.body1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}