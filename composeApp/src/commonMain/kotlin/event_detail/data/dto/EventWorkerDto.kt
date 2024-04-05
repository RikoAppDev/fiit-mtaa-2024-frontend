package event_detail.data.dto

import core.domain.worker.AssignmentStatus
import kotlinx.datetime.Instant
import kotlinx.serialization.*



@Serializable
data class EventWorkerDto (
    val assignmentStatus: AssignmentStatus,
    val createdAt: Instant,
    val user: WorkerUserDto
)

@Serializable
data class WorkerUserDto (
    val email: String,
    val name: String,
    val phoneNumber: String? = null
)