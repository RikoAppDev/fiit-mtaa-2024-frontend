package events_on_map_screen.presentation
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import core.domain.EventMarker

class EventsOnMapScreenComponent(componentContext: ComponentContext):ComponentContext by componentContext {

    private val _markers = MutableValue(
        listOf(
            EventMarker("13", 48.946427, 18.118392),
            EventMarker("133", 48.746427, 18.218392),
            EventMarker("133", 48.846427, 18.318392),
        )
    )
    val markers: Value<List<EventMarker>> = _markers

    fun onEvent(){

    }
}