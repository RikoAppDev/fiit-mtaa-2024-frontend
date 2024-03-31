package core.domain

sealed interface DataError : Error {
    enum class NetworkError : DataError {
        // 3xx - responses
        RESPONSE,

        // 4xx - responses
        BAD_REQUEST,                // 400
        UNAUTHORISED,               // 401
        FORBIDDEN,                  // 403
        NOT_FOUND,                  // 404
        REQUEST_TIMEOUT,            // 408
        CONFLICT,                   // 409
        PAYLOAD_TOO_LARGE,          // 413
        TOO_MANY_REQUESTS,          // 429
        CLIENT,

        // 5xx - responses
        SERVER,

        // Others
        SERIALIZATION,
        NO_INTERNET,
        UNKNOWN
    }

    enum class LocalError : DataError {
        NO_RECORD,
        NOT_LOGGED_IN
    }
}