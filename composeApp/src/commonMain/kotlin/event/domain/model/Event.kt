package event.domain.model

import kotlinx.datetime.LocalDateTime

data class Event(
    val image: ByteArray?,
    val title: String,
    val description: String,
    val capacity: Int,
    val datetime: LocalDateTime?,
    val requiredTools: String?,
    val providedTools: String?,
    val location: EventLocation?,
    val salaryType: SalaryType,
    val salaryAmount: String,
    val salaryUnit: String,
    val salaryGoodTitle: String,
    val categoryList: MutableList<String>?
)
