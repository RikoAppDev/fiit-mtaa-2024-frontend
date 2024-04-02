package core.presentation.components.map_view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import core.domain.EventMarker
import core.domain.GpsPosition
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKAnnotationView
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKPointAnnotation
import platform.darwin.NSObject

class CustomPointAnnotation(val eventId: String) : MKPointAnnotation()
class MapViewDelegate(var onAnnotationClick: (eventId: String) -> Unit) : NSObject(),
    MKMapViewDelegateProtocol {
    @ObjCAction
    override fun mapView(mapView: MKMapView, didSelectAnnotationView: MKAnnotationView) {
        val annotation = didSelectAnnotationView.annotation
        if (annotation is CustomPointAnnotation) {
            onAnnotationClick(annotation.eventId)
        }
    }
}


@OptIn(ExperimentalForeignApi::class, ExperimentalResourceApi::class)
@Composable
actual fun LocationVisualizer(
    modifier: Modifier,
    markers: List<EventMarker>,
    starterPosition: GpsPosition,
    parentScrollEnableState: MutableState<Boolean>,
    onMarkerClick: (eventId: String) -> Boolean
) {
    val mapViewDelegate = remember { MapViewDelegate(onAnnotationClick = { onMarkerClick(it) }) }

    val cameraPositionState = remember {
        mutableStateOf(
            CLLocationCoordinate2DMake(
                starterPosition.latitude,
                starterPosition.longitude
            )
        )
    }

    val mkMapView = remember {
        MKMapView().apply {
            delegate = mapViewDelegate
            setCenterCoordinate(
                CLLocationCoordinate2DMake(
                    starterPosition.latitude,
                    starterPosition.longitude
                )
            )
            markers.forEach { marker ->
                val annotation = CustomPointAnnotation(marker.eventId.orEmpty()).apply {
                    setCoordinate(CLLocationCoordinate2DMake(marker.latitude, marker.longitude))
                }
                addAnnotation(annotation)
            }

        }
    }

    mapViewDelegate.onAnnotationClick = { eventId ->
        onMarkerClick(eventId)
        val marker = markers.find { it.eventId == eventId }
        marker?.let {
            val newCameraPosition = CLLocationCoordinate2DMake(it.latitude, it.longitude)
            cameraPositionState.value = newCameraPosition
            mkMapView.setCenterCoordinate(newCameraPosition, animated = true)

        }
    }

    UIKitView(
        modifier = modifier,
        factory = { mkMapView }
    )
}

