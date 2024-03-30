package core.presentation.components.event_categories

import EventCategoryDto
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import core.presentation.components.category_chip.CategoryChip

@OptIn(ExperimentalLayoutApi::class)
@Composable

fun EventCategories(categories: List<EventCategoryDto>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (categories.isEmpty()) {
            Text(
                "No categories",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary
            )
        }else {
            for (category in categories) {
                CategoryChip(
                    text = "${category.icon} ${category.name}",
                    color= category.colorVariant
                )

            }
        }
    }

}