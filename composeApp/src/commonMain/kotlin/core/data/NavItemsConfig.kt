package core.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.bottom_navigation__harvests
import grabit.composeapp.generated.resources.bottom_navigation__home
import grabit.composeapp.generated.resources.bottom_navigation__map
import grabit.composeapp.generated.resources.bottom_navigation__my_harvests
import grabit.composeapp.generated.resources.menu_all_events
import grabit.composeapp.generated.resources.menu_home
import grabit.composeapp.generated.resources.menu_map
import grabit.composeapp.generated.resources.menu_my_events
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


data class NavItem(
    val id: String,
    val title: String,
    val icon: ImageVector
)

@Composable
@OptIn(ExperimentalResourceApi::class)
fun getNavigationItems(): List<NavItem> {
    return listOf(
        NavItem(
            "home",
            stringResource(Res.string.bottom_navigation__home),
            vectorResource(Res.drawable.menu_home)
        ),
        NavItem(
            "my_harvests",
            stringResource(Res.string.bottom_navigation__harvests),
            vectorResource(Res.drawable.menu_all_events)
        ),
        NavItem(
            "map",
            stringResource(Res.string.bottom_navigation__map),
            vectorResource(Res.drawable.menu_map)
        ),
        NavItem(
            "my_harvests",
            stringResource(Res.string.bottom_navigation__my_harvests),
            vectorResource(Res.drawable.menu_my_events)
        ),
    )
}