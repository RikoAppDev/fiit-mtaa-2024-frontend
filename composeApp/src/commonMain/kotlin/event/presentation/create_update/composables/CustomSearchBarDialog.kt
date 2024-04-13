package event.presentation.create_update.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBarDialog(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: SearchBarColors = SearchBarDefaults.colors(),
    shape: Shape = Shapes.medium,
    onDismissRequest: () -> Unit,
    containerColor: Color = MaterialTheme.colors.background,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = shape,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    shape = shape,
                    color = containerColor
                ),
            color = containerColor
        ) {
            SearchBar(
                query = query,
                onQueryChange = {
                    onQueryChange(it)
                },
                onSearch = {
                    onSearch(it)
                },
                active = active,
                onActiveChange = {
                    onActiveChange(it)
                },
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                shape = Shapes.medium,
                colors = colors,
                modifier = Modifier
            ) {
                content()
            }
        }
    }
}