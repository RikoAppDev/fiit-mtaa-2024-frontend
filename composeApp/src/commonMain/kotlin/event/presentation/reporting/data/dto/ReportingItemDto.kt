package event.presentation.reporting.data.dto


import SallaryObject
import kotlinx.datetime.Instant
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class ReportingItemsListDto (
    val reportingItems: List<ReportingItemDto>,
    val sallary: SallaryObject
)

@Serializable
data class ReportingItemDto (
    val arrivedAt: Instant,
    val hoursWorked: Long,
    val leftAt: Instant,
    val user: ReportingUserDto
)

@Serializable
data class ReportingUserDto (
    val id: String,
    val name: String
)