package core.data.remote

import core.data.remote.dto.EventCardListDto
import account_detail.domain.model.UpdateUser
import all_events_screen.data.CategoriesWithCountDto
import auth.data.remote.dto.AuthUserDto
import auth.domain.model.Login
import auth.domain.model.NewUser
import core.domain.event.SallaryType
import event_detail.data.dto.EventDetailDto
import event_detail.data.dto.EventWorkersDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

private const val TIMEOUT = 15_000L

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

    suspend fun getLatestEvents(token: String): EventCardListDto = withContext(Dispatchers.IO) {
        return@withContext client
            .get(UrlHelper.GetLatestEventsUrl.path) {
                header("Authorization", "Bearer $token")
            }.body()


    }

    suspend fun getEventDetail(id: String, token: String): EventDetailDto =
        withContext(Dispatchers.IO) {
            return@withContext client.get("events/$id") {
                header("Authorization", "Bearer $token")
            }.body<EventDetailDto>()
        }

    suspend fun getEventWorkers(id: String, token: String): EventWorkersDto =
        withContext(Dispatchers.IO) {
            return@withContext client.get("events/$id/workers") {
                header("Authorization", "Bearer $token")
            }.body<EventWorkersDto>()
        }

    suspend fun signInForEvent(id: String, token: String): String = withContext(Dispatchers.IO) {
        return@withContext client.post("events/$id/signFor") {
            header("Authorization", "Bearer $token")
        }.body<String>()
    }

    suspend fun signOffEvent(id: String, token: String): String = withContext(Dispatchers.IO) {
        return@withContext client.post("events/$id/signOff") {
            header("Authorization", "Bearer $token")
        }.body<String>()
    }

    suspend fun getCategoriesWithCount(token: String): CategoriesWithCountDto =
        withContext(Dispatchers.IO) {
            return@withContext client.get("events/categories") {
                header("Authorization", "Bearer $token")
            }.body<CategoriesWithCountDto>()
        }

    suspend fun getEventsFiltered(
        token: String,
        filterCategory: String?,
        filterSallary: SallaryType?,
        filterDistance: Number?
    ): EventCardListDto = withContext(Dispatchers.IO) {


        return@withContext client.get("events/") {
            header("Authorization", "Bearer $token")
            url {
                if (filterCategory != null) parameters.append("categoryID", filterCategory)
                if (filterDistance != null) parameters.append("distance", filterDistance.toString())
                if (filterSallary != null) parameters.append("priceType", filterSallary.toString())
            }
        }.body<EventCardListDto>()
    }

}