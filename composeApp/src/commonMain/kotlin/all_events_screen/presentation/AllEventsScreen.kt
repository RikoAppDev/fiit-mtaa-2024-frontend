package all_events_screen.presentation

import all_events_screen.presentation.component.AllEventScreenComponent
import all_events_screen.presentation.component.AllEventScreenEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.data.getNavigationItems
import core.data.helpers.event.printifyEventDateTime
import core.domain.event.SallaryType
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.event_card.EventCard
import core.presentation.components.logo.GrabItLogo
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.all
import grabit.composeapp.generated.resources.all_events_screen__filter_apply
import grabit.composeapp.generated.resources.all_events_screen__filter_by_category
import grabit.composeapp.generated.resources.all_events_screen__filter_by_distance
import grabit.composeapp.generated.resources.all_events_screen__filter_by_sallary
import grabit.composeapp.generated.resources.all_events_screen__filter_reset
import grabit.composeapp.generated.resources.event_detail_screen__signed_at
import grabit.composeapp.generated.resources.filter
import grabit.composeapp.generated.resources.hide_filter
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.profile
import grabit.composeapp.generated.resources.salary_goods
import grabit.composeapp.generated.resources.sallary_type_money
import grabit.composeapp.generated.resources.show_filter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ui.data.getButtonColors
import ui.domain.ColorVariation
import ui.theme.MenuActive

@OptIn(
    ExperimentalResourceApi::class, ExperimentalLayoutApi::class,

    )
@Composable
fun AllEventsScreen(component: AllEventScreenComponent) {
    val topBarModifier = if (isSystemInDarkTheme()) {
        Modifier.background(MaterialTheme.colors.background).displayCutoutPadding().height(80.dp)
    } else {
        Modifier.background(Color.White).displayCutoutPadding().height(80.dp).shadow(
            elevation = 16.dp, spotColor = Color(0x40E9E9E9), ambientColor = Color(0x40E9E9E9)
        )
    }

    val allEventsState by component.allEventsState.subscribeAsState()
    var selected by mutableStateOf(1)

    var filterVisible by mutableStateOf(false)

    var selectedFilterCategory by mutableStateOf<String?>(null)
    var selectedFilterSallary by mutableStateOf<SallaryType?>(null)
    var selectedFilterDistance by mutableStateOf<Number?>(null)

    val bottomBarModifier = if (isSystemInDarkTheme()) {
        Modifier
    } else {
        Modifier.shadow(
            elevation = 16.dp, spotColor = Color(0x40CACACA), ambientColor = Color(0x40CACACA)
        )
    }

    LaunchedEffect(true) {
        component.loadCategoriesWithCount()
        component.loadFilteredEvents(
            selectedFilterCategory, selectedFilterSallary, selectedFilterDistance
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = topBarModifier,
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.background
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight()
                        .padding(start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(Modifier.width(120.dp)) {
                        GrabItLogo()
                    }
                    Box(
                        Modifier.clip(CircleShape).clickable(onClick = {
                            null
                        })

                            .background(MaterialTheme.colors.surface).padding(6.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = vectorResource(Res.drawable.profile),
                            contentDescription = stringResource(Res.string.logo),
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                modifier = bottomBarModifier,
                backgroundColor = MaterialTheme.colors.background,
            ) {
                Row(
                    Modifier.navigationBarsPadding()
                        .padding(bottom = 16.dp, start = 24.dp, end = 24.dp, top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    getNavigationItems().forEachIndexed { index, item ->
                        BottomNavigationItem(icon = {
                            Icon(
                                item.icon, contentDescription = item.title
                            )
                        },
                            label = {
                                Text(
                                    text = item.title, style = TextStyle(
                                        fontFamily = MaterialTheme.typography.body2.fontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 10.sp
                                    )
                                )
                            },
                            selected = index == selected,
                            selectedContentColor = MenuActive,
                            unselectedContentColor = MaterialTheme.colors.secondary,
                            onClick = {
                                selected = index
                            },
                            alwaysShowLabel = false
                        )
                    }
                }
            }
        },


        ) { paddingValues ->
        Box(
            Modifier.fillMaxSize().padding(
                start = 24.dp, end = 24.dp, bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            Column {
                FilterButton(filterVisible, onFilterToggle = { filterVisible = !filterVisible })

                AnimatedVisibility(
                    visible = filterVisible, enter = expandVertically(
                        // Customize the animation further if you like
                        animationSpec = tween(durationMillis = 150)
                    ), exit = shrinkVertically(
                        // Customize the animation further if you like
                        animationSpec = tween(durationMillis = 150)
                    )
                ) {
                    Column(
                        Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(Res.string.all_events_screen__filter_by_category),
                                style = MaterialTheme.typography.h2,
                                color = MaterialTheme.colors.onBackground
                            )
                            if (!allEventsState.isLoadingCategories) {
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(-4.dp),
                                ) {
                                    CustomFilterChip(
                                        text = stringResource(Res.string.all),
                                        isSelected = selectedFilterCategory == null,
                                        onClick = { selectedFilterCategory = null },
                                    )
                                    allEventsState.categories!!.categories!!.forEach { event ->
                                        CustomFilterChip(
                                            text = "${event.icon} ${event.name} (${event.count.eventCategoryRelation})",
                                            isSelected = selectedFilterCategory == event.id,
                                            onClick = { selectedFilterCategory = event.id },
                                            colorVariant = event.colorVariant
                                        )
                                    }

                                }

                            }

                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(Res.string.all_events_screen__filter_by_sallary),
                                style = MaterialTheme.typography.h2,
                                color = MaterialTheme.colors.onBackground
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(-8.dp),
                            ) {
                                CustomFilterChip(
                                    text = stringResource(Res.string.all),
                                    isSelected = selectedFilterSallary == null,
                                    onClick = { selectedFilterSallary = null },
                                )
                                CustomFilterChip(
                                    text = stringResource(Res.string.sallary_type_money),
                                    isSelected = selectedFilterSallary == SallaryType.MONEY,
                                    onClick = {
                                        selectedFilterSallary = SallaryType.MONEY
                                    },
                                )
                                CustomFilterChip(
                                    text = stringResource(Res.string.salary_goods),
                                    isSelected = selectedFilterSallary == SallaryType.GOODS,
                                    onClick = {
                                        selectedFilterSallary = SallaryType.GOODS
                                    },
                                )

                            }


                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(Res.string.all_events_screen__filter_by_distance),
                                style = MaterialTheme.typography.h2,
                                color = MaterialTheme.colors.onBackground
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(-8.dp),
                            ) {
                                CustomFilterChip(
                                    text = stringResource(Res.string.all),
                                    isSelected = selectedFilterDistance == null,
                                    onClick = { selectedFilterDistance = null },
                                )

                                listOf(5, 10, 25, 50, 100).forEach { distance ->
                                    CustomFilterChip(
                                        text = "$distance km",
                                        isSelected = selectedFilterDistance == distance,
                                        onClick = { selectedFilterDistance = distance },
                                    )
                                }
                            }
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ButtonPrimary(buttonModifier = Modifier.weight(0.5f),
                                type = ColorVariation.CHERRY,
                                text = stringResource(Res.string.all_events_screen__filter_reset),
                                onClick = {
                                    selectedFilterCategory = null
                                    selectedFilterSallary = null
                                    selectedFilterDistance = null
                                })

                            ButtonPrimary(buttonModifier = Modifier.weight(0.5f),
                                type = ColorVariation.APPLE,
                                text = stringResource(Res.string.all_events_screen__filter_apply),
                                onClick = {
                                    component.onEvent(
                                        AllEventScreenEvent.ApplyFilter(
                                            selectedFilterCategory,
                                            selectedFilterSallary,
                                            selectedFilterDistance
                                        )
                                    )
                                    filterVisible = false
                                })
                        }
                    }
                }

                if (!allEventsState.isLoadingEvents) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        itemsIndexed(allEventsState.events!!.events) { index, event ->
                            EventCard(
                                event,
                                { component.onEvent(AllEventScreenEvent.EventDetailScreen(event.id)) },
                                isVertical = false
                            )
                        }
                    }
                } else {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomFilterChip(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    colorVariant: ColorVariation = ColorVariation.LIME
) {
    val filterChipsColors = getButtonColors(colorVariant)
    FilterChip(onClick = onClick, selected = isSelected, colors = ChipDefaults.filterChipColors(
        backgroundColor = Color.Transparent,
        selectedBackgroundColor = filterChipsColors.backgroundColor,
        selectedContentColor = filterChipsColors.textColor
    ), border = BorderStroke(1.dp, filterChipsColors.backgroundColor), content = {
        Text(
            style = MaterialTheme.typography.body1, text = text
        )
    })
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun FilterButton(filterVisible: Boolean, onFilterToggle: () -> Unit) {
    val arrowRotationDegree by animateFloatAsState(
        targetValue = if (filterVisible) 180f else 0f, animationSpec = tween(durationMillis = 300)
    )

    val text = if (filterVisible) stringResource(Res.string.hide_filter)
    else stringResource(Res.string.show_filter)

    IconButton(onClick = { onFilterToggle() }) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                modifier = Modifier.padding(end = 8.dp),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onBackground
            )
            Icon(
                imageVector = Icons.Rounded.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.size(24.dp).graphicsLayer(
                    rotationZ = arrowRotationDegree
                )
            )
        }
    }
}

