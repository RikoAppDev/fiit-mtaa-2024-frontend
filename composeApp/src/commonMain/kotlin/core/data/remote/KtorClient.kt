package core.data.remote

import core.data.remote.dto.EventCardListDto
import account_detail.domain.model.UpdateUser
import all_events_screen.data.CategoriesWithCountDto
import auth.data.remote.dto.AuthUserDto
import auth.domain.model.Login
import auth.domain.model.NewUser
import event.data.dto.EventDetailDto
import event.data.dto.EventWorkersDto
import core.domain.event.SallaryType
import event.data.dto.CategoriesDto
import event.data.dto.EventCreateUpdateDto
import event.data.dto.EventCreateUpdateRespDto
import event.data.dto.AttendanceDataDto
import event.data.dto.ImageUploadDto
import event.data.dto.LiveEventDataDto
import event.data.dto.PlacesResponseDto
import events_on_map_screen.data.PointListDto
import home_screen.data.ActiveEventDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

private const val TIMEOUT = 5_000L

object KtorClient {
    private val client = HttpClient {
        expectSuccess = true
        defaultRequest {
            url.takeFrom(
                url = URLBuilder().takeFrom(
                    urlString = UrlHelper.BaseUrl.path
                )
            )
            contentType(
                type = ContentType.Application.Json
            )
        }
        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }

    suspend fun loginUser(login: Login): AuthUserDto = withContext(Dispatchers.IO) {
        val authUserDto: AuthUserDto = client.post(UrlHelper.LoginUserUrl.path) {
            setBody(login)
        }.body()

        return@withContext authUserDto
    }

    suspend fun registerUser(newUser: NewUser): AuthUserDto = withContext(Dispatchers.IO) {
        val authUserDto: AuthUserDto = client.post(UrlHelper.CreateAccountUrl.path) {
            setBody(newUser)
        }.body()

        return@withContext authUserDto
    }

    suspend fun verifyUserToken(token: String): Boolean = withContext(Dispatchers.IO) {
        val response = client.get(UrlHelper.UserVerifyTokenUrl.path) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }

        return@withContext response.status.value == 200
    }

    suspend fun updateUser(updateUserData: UpdateUser, token: String): String =
        withContext(Dispatchers.IO) {
            val response: String = client.put(UrlHelper.UpdateUserUrl.path) {
                setBody(updateUserData)
                header("Authorization", "Bearer $token")
            }.body()

            return@withContext response
        }

    suspend fun deleteUserAccount(token: String): String = withContext(Dispatchers.IO) {
        return@withContext client.delete("user/") {
            header("Authorization", "Bearer $token")
        }.body<String>()
    }

    suspend fun getLatestEvents(token: String): EventCardListDto = withContext(Dispatchers.IO) {
        return@withContext client.get(UrlHelper.GetLatestEventsUrl.path) {
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun getEventDetail(id: String, token: String): EventDetailDto =
        withContext(Dispatchers.IO) {
            return@withContext client.get(UrlHelper.GetEventDetailUrl.withEventId(id)) {
                header("Authorization", "Bearer $token")
            }.body<EventDetailDto>()
        }

    suspend fun getEventWorkers(id: String, token: String): EventWorkersDto =
        withContext(Dispatchers.IO) {
            return@withContext client.get(UrlHelper.GetEventWorkersUrl.withEventId(id)) {
                header("Authorization", "Bearer $token")
            }.body<EventWorkersDto>()
        }

    suspend fun signInForEvent(id: String, token: String): String = withContext(Dispatchers.IO) {
        return@withContext client.post(UrlHelper.SignInForEventUrl.withEventId(id)) {
            header("Authorization", "Bearer $token")
        }.body<String>()
    }

    suspend fun signOffEvent(id: String, token: String): String = withContext(Dispatchers.IO) {
        return@withContext client.post(UrlHelper.SignOffEventUrl.withEventId(id)) {
            header("Authorization", "Bearer $token")
        }.body<String>()
    }

    suspend fun getCategoriesWithCount(token: String): CategoriesWithCountDto =
        withContext(Dispatchers.IO) {
            return@withContext client.get(UrlHelper.GetCategoriesUrl.path) {
                header("Authorization", "Bearer $token")
            }.body<CategoriesWithCountDto>()
        }

    suspend fun uploadImage(token: String, imageData: ByteArray): ImageUploadDto =
        withContext(Dispatchers.IO) {
            return@withContext client.post(UrlHelper.UploadImageUrl.path) {
                header("Authorization", "Bearer $token")
                contentType(ContentType.MultiPart.FormData)
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                "image",
                                imageData,
                                Headers.build {
                                    append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                                }
                            )
                        }
                    )
                )
            }.body<ImageUploadDto>()
        }

    suspend fun getEventsFiltered(
        token: String,
        filterCategory: String?,
        filterSallary: SallaryType?,
        filterDistance: Number?
    ): EventCardListDto = withContext(Dispatchers.IO) {
        return@withContext client.get(UrlHelper.GetEventsUrl.path) {
            header("Authorization", "Bearer $token")
            url {
                if (filterCategory != null) parameters.append("categoryID", filterCategory)
                if (filterDistance != null) parameters.append("distance", filterDistance.toString())
                if (filterSallary != null) parameters.append("priceType", filterSallary.toString())
            }
        }.body<EventCardListDto>()
    }

    suspend fun getPointsOnMap(token: String): PointListDto =
        withContext(Dispatchers.IO) {
            return@withContext client.get(UrlHelper.GetMapEventsUrl.path) {
                header("Authorization", "Bearer $token")
            }.body<PointListDto>()
        }

    suspend fun getMyEvents(token: String): EventCardListDto =
        withContext(Dispatchers.IO) {
            return@withContext client.get(UrlHelper.GetMyEventsUrl.path) {
                header("Authorization", "Bearer $token")
            }.body<EventCardListDto>()
        }

    suspend fun startEvent(id: String, token: String): String = withContext(Dispatchers.IO) {
        return@withContext client.put(UrlHelper.StartEventUrl.withEventId(id)) {
            header("Authorization", "Bearer $token")
        }.body<String>()
    }

    suspend fun getActiveEvent(token: String): ActiveEventDto = withContext(Dispatchers.IO) {
        return@withContext client.get(UrlHelper.GetActiveEventUrl.path) {
            header("Authorization", "Bearer $token")
        }.body<ActiveEventDto>()
    }

    suspend fun createEvent(
        eventCreateUpdateDto: EventCreateUpdateDto,
        token: String
    ): EventCreateUpdateRespDto = withContext(Dispatchers.IO) {
        val respDto: EventCreateUpdateRespDto = client.post(UrlHelper.CreateEventUrl.path) {
            header("Authorization", "Bearer $token")
            setBody(eventCreateUpdateDto)
        }.body()

        return@withContext respDto
    }

    suspend fun updateEvent(
        eventCreateUpdateDto: EventCreateUpdateDto,
        id: String,
        token: String
    ) = withContext(Dispatchers.IO) {
        return@withContext client.post(UrlHelper.UpdateEventUrl.withEventId(id)) {
            header("Authorization", "Bearer $token")
            setBody(eventCreateUpdateDto)
        }
    }

    suspend fun getAllCategories(token: String): CategoriesDto = withContext(Dispatchers.IO) {
        return@withContext client.get(UrlHelper.GetCategoriesUrl.path) {
            header("Authorization", "Bearer $token")
        }.body<CategoriesDto>()
    }

    suspend fun getLiveEventData(id: String, token: String): LiveEventDataDto =
        withContext(Dispatchers.IO) {
            return@withContext client.get(UrlHelper.LiveEventUrl.withEventId(id)) {
                header("Authorization", "Bearer $token")
            }.body<LiveEventDataDto>()
        }

    suspend fun getAttendanceData(id: String, token: String): AttendanceDataDto =
        withContext(Dispatchers.IO) {
            return@withContext client.get(UrlHelper.GetAttendanceUrl.withEventId(id)) {
                header("Authorization", "Bearer $token")
            }.body<AttendanceDataDto>()
        }

    suspend fun getPlaces(query: String, token: String): PlacesResponseDto =
        withContext(Dispatchers.IO) {
            return@withContext client.get(UrlHelper.GetPlacesUrl.path) {
                header("Authorization", "Bearer $token")
                url {
                    parameters.append("q", query)
                }
            }.body()
        }
}