package event.data.dto

import core.domain.worker.AssignmentStatus
import event.domain.model.PresenceStatus
import kotlinx.datetime.Instant

import kotlinx.serialization.*
@Serializable
data class AttendanceDataDto (
    val workers: List<AttendanceWorkerDto>,
    var lastUpdated: Instant
)

@Serializable
data class AttendanceWorkerDto (

    var arrivedAt: Instant? = null,

    var assignmentStatus: AssignmentStatus,
    var presenceStatus: PresenceStatus,

    var leftAt: Instant? = null,

    val user: AttendanceUserDto
)



@Serializable
data class AttendanceUserDto (
    val id: String,
    val name: String
)


@Serializable
data class AttendanceUpdateListDto (
    val workers: List<AttendanceUpdateDto>
)

@Serializable
data class AttendanceUpdateDto (
    val arrivedAt: String? = null,
    val leftAt: String? = null,
    val presenceStatus: PresenceStatus,
    val userID: String
)