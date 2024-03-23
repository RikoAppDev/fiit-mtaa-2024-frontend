package core.data.remote

import auth.domain.model.NewUser
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val TIMEOUT = 15_000L

object KtorClient {
    private val client = HttpClient {
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
            this.level = LogLevel.ALL
            this.logger = Logger.Companion.DEFAULT
        }
    }

    suspend fun registerUser(newUser: NewUser) = withContext(Dispatchers.IO) {
        @Serializable
        class RequestBody(
            val email: String,
            val password: String,
            val name: String
        )

        @Serializable
        class ResponseBody(
            val token: String,
        )

        val response = client.post(UrlHelper.CreateAccountUrl.path) {
            setBody(RequestBody("jako@gmail.com", ":D", "pagastan"))
        }

        println(response)

        return@withContext response
    }
}