package core.presentation.components.map_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.grabit.R
import core.domain.EventMarker
import core.domain.GpsPosition
import events_on_map_screen.data.getMapStyle


@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun LocationVisualizer(
    modifier: Modifier,
    markers: List<EventMarker>,
    starterPosition: GpsPosition,
    parentScrollEnableState: MutableState<Boolean>,
    onMarkerClick: (marker: String) -> Boolean
) {
    val currentLocation = LatLng(starterPosition.latitude, starterPosition.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 10f)
    }
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            parentScrollEnableState.value = true
        }
    }
    GoogleMap(
        modifier = modifier.pointerInteropFilter(
            onTouchEvent = {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        parentScrollEnableState.value = false
                        false
                    }

                    else -> true
                }
            },
        ),
        properties = MapProperties(
            isBuildingEnabled = false,
            mapStyleOptions = MapStyleOptions(
                getMapStyle()
            )
        ),
        cameraPositionState = cameraPositionState,

        ) {
        markers.forEach { marker ->
            Marker(
                onClick = {
                    onMarkerClick(marker.eventId as String)
                    false
                },
                state = MarkerState(position = LatLng(marker.latitude, marker.longitude)),
                icon = BitmapFromVector(LocalContext.current, R.drawable.map_marker_light)
            )

        }
    }
}


fun BitmapFromVector(context: Context?, vectorResId: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(
        context!!, vectorResId
    )

    vectorDrawable!!.setBounds(
        0, 0, vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight
    )

    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)

    vectorDrawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}