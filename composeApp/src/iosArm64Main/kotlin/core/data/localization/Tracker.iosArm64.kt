package core.data.localization

import dev.icerock.moko.geo.ExtendedLocation
import dev.icerock.moko.geo.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.darwin.NSObject

internal actual class Tracker actual constructor(
    locationsChannel: Channel<LatLng>,
    extendedLocationsChannel: Channel<ExtendedLocation>,
    scope: CoroutineScope
) : NSObject(), CLLocationManagerDelegateProtocol