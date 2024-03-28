package core.domain

typealias RootError = Error

sealed interface ResultHandler<out D, out E : RootError> {
    data class Success<out D, out E : RootError>(val data: D) : ResultHandler<D, E>
    data class Error<out D, out E : RootError>(val error: E) : ResultHandler<D, E>
    data class Loading<out D, out E : RootError>(val data: D? = null) : ResultHandler<D, E>
}