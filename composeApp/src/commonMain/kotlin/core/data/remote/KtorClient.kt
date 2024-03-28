package core.data.remote

import auth.data.remote.dto.RegisterUserDto
import auth.domain.model.NewUser
import core.domain.Error
import core.domain.ResultHandler
import core.domain.RootError
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import io.ktor.util.Identity.decode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.internal.decodeStringToJsonTree
import kotlinx.serialization.json.internal.readJson
import kotlin.math.log

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
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v("HTTP Client", null, message)
                }
            }
        }
    }.also { Napier.base(DebugAntilog()) }

    suspend fun registerUser(newUser: NewUser): RegisterUserDto = withContext(Dispatchers.IO) {
        val registerUserDto: RegisterUserDto = client.post(UrlHelper.CreateAccountUrl.path) {
            setBody(newUser)
        }.body()

        return@withContext registerUserDto
    }
}