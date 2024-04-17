package core.data.remote

import com.grabit.BuildKonfig


sealed class UrlHelper(val path: String) {
    data object BaseUrl : UrlHelper(BuildKonfig.BASE_URL)
    data object CreateAccountUrl : UrlHelper("createAccount/")
    data object LoginUserUrl : UrlHelper("login/")
    data object UserVerifyTokenUrl : UrlHelper("user/verifyToken")
    data object UpdateUserUrl : UrlHelper("user/editAccount")

    data object GetEventsUrl : UrlHelper("events")
    data object GetLatestEventsUrl : UrlHelper("events/latest")
    data object GetActiveEventUrl : UrlHelper("events/active")

    data object GetEventDetailUrl : UrlHelper("events/{eventId}")
    data object GetEventWorkersUrl : UrlHelper("events/{eventId}/workers")

    data object SignInForEventUrl : UrlHelper("events/{eventId}/signFor")
    data object SignOffEventUrl : UrlHelper("events/{eventId}/signOff")

    data object StartEventUrl : UrlHelper("events/{eventId}/startEvent")
    data object EndEventUrl : UrlHelper("events/{eventId}/endEvent")

    data object UploadImageUrl : UrlHelper("events/uploadImage")
    data object CreateEventUrl : UrlHelper("events/create")
    data object UpdateEventUrl : UrlHelper("events/{eventId}/update")
    data object DeleteEventUrl : UrlHelper("events/{eventId}")

    data object GetCategoriesUrl : UrlHelper("events/categories")
    data object GetMapEventsUrl : UrlHelper("events/onMap")
    data object GetMyEventsUrl : UrlHelper("events/my")

    data object LiveEventUrl : UrlHelper("events/{eventId}/live")
    data object GetAttendanceUrl : UrlHelper("events/{eventId}/attendance")
    data object UpdateAttendanceUrl : UrlHelper("events/{eventId}/updateAttendance")

    data object GetPlacesUrl : UrlHelper("searchPlaces")

    fun withEventId(eventId: String): String {
        return buildString {
            append(path.replace("{eventId}", eventId))
        }
    }
}