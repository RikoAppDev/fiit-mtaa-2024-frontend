package event.domain.model

enum class PresenceStatus {
    NOT_PRESENT,
    DID_NOT_ARRIVE,
    PRESENT,
    LEFT
}

fun convertToPresenceStatus(status: String): PresenceStatus? {
    return when (status) {
        "NOT_PRESENT" -> PresenceStatus.NOT_PRESENT
        "DID_NOT_ARRIVE" -> PresenceStatus.DID_NOT_ARRIVE
        "PRESENT" -> PresenceStatus.PRESENT
        "LEFT" -> PresenceStatus.LEFT
        else -> null
    }
}