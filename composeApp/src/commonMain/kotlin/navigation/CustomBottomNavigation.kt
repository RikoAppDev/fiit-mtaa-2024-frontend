package navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.MenuActive

@Composable
fun CustomBottomNavigation(
    selectedItemIndex: Int,
    onNavigateEvent: (BottomNavigationEvent) -> Unit
) {
    val bottomBarModifier = if (isSystemInDarkTheme()) {
        Modifier
    } else {
        Modifier.shadow(
            elevation = 16.dp, spotColor = Color(0x40CACACA), ambientColor = Color(0x40CACACA)
        )
    }

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
                BottomNavigationItem(
                    icon = {
                        Icon(item.icon, contentDescription = item.title)
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = TextStyle(
                                fontFamily = MaterialTheme.typography.body2.fontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp
                            )
                        )
                    },
                    selected = selectedItemIndex == index,
                    selectedContentColor = MenuActive,
                    unselectedContentColor = MaterialTheme.colors.secondary,
                    onClick = {
                        onNavigateEvent(item.event)
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}