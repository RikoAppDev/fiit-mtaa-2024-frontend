package core.presentation.components.event_categories

import core.data.remote.dto.EventCategoryDto
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import core.presentation.components.category_chip.CategoryChip
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_detail_screen__no_categories
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class)
@Composable
fun EventCategories(
    categories: List<EventCategoryDto>,
    removable: Boolean = false,
    onCategoryClick: ((index: Int) -> Unit)? = null
) {
    val space = if (removable) {
        24.dp
    } else {
        8.dp
    }

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(space),
        verticalArrangement = Arrangement.spacedBy(space)
    ) {
        if (categories.isEmpty()) {
            Text(
                stringResource(Res.string.event_detail_screen__no_categories),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary
            )
        } else {
            categories.forEachIndexed { index, category ->
                CategoryChip(
                    text = "${category.icon} ${category.name}",
                    color = category.colorVariant,
                    removable = removable,
                    onClick = {
                        if (onCategoryClick != null) {
                            onCategoryClick(index)
                        }
                    }
                )
            }
        }
    }
}