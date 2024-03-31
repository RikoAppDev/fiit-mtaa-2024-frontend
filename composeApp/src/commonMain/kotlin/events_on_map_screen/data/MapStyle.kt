package events_on_map_screen.data

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
fun getMapStyle(): String {
    val darkTheme = "[\n" +
            "  {\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#242f3e\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#746855\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"elementType\": \"labels.text.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#242f3e\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"administrative.locality\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#d59563\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#d59563\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.business\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"visibility\": \"off\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#263c3f\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"labels.text\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"visibility\": \"off\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#6b9a76\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#38414e\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"geometry.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#212a37\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#9ca5b3\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#746855\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"geometry.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#1f2835\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#f3d19c\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"transit\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#2f3948\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"transit.station\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#d59563\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"water\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#17263c\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"water\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#515c6d\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"water\",\n" +
            "    \"elementType\": \"labels.text.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#17263c\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]"

    val lightTheme = "[\n" +
            "  {\n" +
            "    \"featureType\": \"poi.business\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"visibility\": \"off\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"labels.text\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"visibility\": \"off\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]"

    val theme = if (isSystemInDarkTheme()) {
        darkTheme
    } else {
        lightTheme
    }

    return theme
}