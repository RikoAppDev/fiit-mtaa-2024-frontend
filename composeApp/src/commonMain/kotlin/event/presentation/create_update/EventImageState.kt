package event.presentation.create_update

data class EventImageState(
    val image: ByteArray?,
    val imageUrl: String?,
    val isLoading:Boolean
)
