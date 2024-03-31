package core.presentation.components.map_view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import core.domain.EventMarker
import core.domain.GpsPosition

@Composable
expect fun LocationVisualizer(
    modifier: Modifier,
    markers: List<EventMarker>,
    starterPosition: GpsPosition,
    parentScrollEnableState: MutableState<Boolean>,
    onMarkerClick: (marker: String) -> Boolean
)
