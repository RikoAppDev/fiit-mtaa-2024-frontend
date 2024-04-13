package event.data.dto

import core.domain.worker.AssignmentStatus
import event.domain.model.PresenceStatus
import kotlinx.datetime.Instant

import kotlinx.serialization.*
@Serializable
data class AttendanceDataDto (
    val workers: List<AttendanceWorkerDto>
)

@Serializable
data class AttendanceWorkerDto (

    val arrivedAt: Instant? = null,

    val assignmentStatus: AssignmentStatus,
    val presenceStatus: PresenceStatus,

    val leftAt: Instant? = null,

    val user: AttendanceUserDto
)



@Serializable
data class AttendanceUserDto (
    val id: String,
    val name: String
)