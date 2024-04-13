package event.domain.model

import kotlinx.datetime.Instant

data class HarmonogramItemLocalized(
    val title: String,
    val from: Instant,
    val to: Instant,
    val isActive: Boolean
)