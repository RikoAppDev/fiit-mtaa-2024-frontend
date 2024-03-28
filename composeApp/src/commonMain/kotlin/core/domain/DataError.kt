package core.domain

interface DataError : Error {
    enum class NetworkError : DataError {
        REDIRECT,
        BAD_REQUEST,
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }
}