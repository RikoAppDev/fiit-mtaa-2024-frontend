package event.presentation.create_update.component

import core.data.remote.dto.EventCategoryDto
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.domain.ResultHandler
import core.domain.event.SallaryType
import core.presentation.error_string_mapper.asUiText
import event.data.dto.EventCreateUpdateDto
import event.data.dto.PlaceDto
import event.domain.CreateUpdateEventValidation
import event.domain.use_case.CreateEventUseCase
import event.domain.use_case.GetAllCategoriesUseCase
import event.domain.use_case.GetPlacesUseCase
import event.domain.use_case.UpdateEventUseCase
import event.domain.use_case.UploadImageUseCase
import event.presentation.create_update.EventCreateUpdateState
import event.presentation.create_update.EventImageState
import event.presentation.create_update.EventState
import event.presentation.create_update.toEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class EventCreateUpdateScreenComponent(
    componentContext: ComponentContext,
    private val uploadImageUseCase: UploadImageUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val updateEventUseCase: UpdateEventUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getPlacesUseCase: GetPlacesUseCase,
    private val createUpdateEventValidation: CreateUpdateEventValidation,
    eventId: String?,
    event: EventState?,
    private val onNavigateToDetailScreen: (eventId: String) -> Unit,
    private val onNavigateBack: () -> Unit
) : ComponentContext by componentContext {
    private val _eventId = MutableValue(eventId ?: "")

    private val _stateIsUpdate = MutableValue(event != null)
    val stateIsUpdate: Value<Boolean> = _stateIsUpdate

    private val _stateCategoriesSize = MutableValue(
        if (stateIsUpdate.value) {
            event!!.categoryList.size
        } else {
            0
        }
    )
    val stateCategoriesSize: Value<Int> = _stateCategoriesSize

    private val _stateEventCreateUpdate =
        MutableValue(
            EventCreateUpdateState(
                isLoading = false,
                event = null,
                error = null
            )
        )
    val stateEventCreateUpdate: Value<EventCreateUpdateState> = _stateEventCreateUpdate

    private val _stateEventImage =
        MutableValue(
            EventImageState(
                image = null,
                imageUrl = null
            )
        )
    val stateEventImage: Value<EventImageState> = _stateEventImage

    private val _stateEvent = MutableValue(
        if (event != null) {
            EventState(
                imageUrl = event.imageUrl,
                title = event.title,
                description = event.description,
                capacity = event.capacity,
                date = event.date,
                time = event.time,
                requiredTools = event.requiredTools,
                providedTools = event.providedTools,
                placeId = event.placeId,
                locationName = event.locationName,
                salaryType = event.salaryType,
                salaryAmount = event.salaryAmount,
                salaryUnit = event.salaryUnit,
                salaryGoodTitle = event.salaryGoodTitle,
                categoryList = event.categoryList
            )
        } else {
            EventState(
                imageUrl = null,
                title = "",
                description = "",
                capacity = "",
                date = null,
                time = null,
                requiredTools = "",
                providedTools = "",
                placeId = "",
                locationName = "",
                salaryType = SallaryType.MONEY,
                salaryAmount = "",
                salaryUnit = "",
                salaryGoodTitle = "",
                categoryList = mutableListOf()
            )
        }
    )
    val stateEvent: Value<EventState> = _stateEvent

    private val _searchCategory = MutableStateFlow("")
    val searchCategory = _searchCategory.asStateFlow()

    private val _categories = MutableStateFlow(listOf<EventCategoryDto>())
    val categories = _searchCategory
        .combine(_categories) { query, categories ->
            if (query.isBlank()) {
                categories
            } else {
                categories.filter {
                    it.doesMatchSearchQuery(query)
                }
            }
        }.stateIn(coroutineScope(), SharingStarted.WhileSubscribed(5000), _categories.value)

    private val _searchLocation = MutableStateFlow("")
    val searchLocation = _searchLocation.asStateFlow()

    private val _isSearching = MutableValue(false)
    val isSearching: Value<Boolean> = _isSearching

    private val _locations = MutableStateFlow(listOf<PlaceDto>())
    val locations = _searchLocation
        .debounce(500)
        .onEach { _isSearching.update { true } }
        .combine(_locations) { query, locations ->
            if (query.isBlank()) {
                locations
            } else {
                getPlaces(query)
                locations
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(coroutineScope(), SharingStarted.WhileSubscribed(5000), _locations.value)

    fun onEvent(event: EventCreateUpdateScreenEvent) {
        when (event) {
            is EventCreateUpdateScreenEvent.OnBackButtonClick -> {
                onNavigateBack()
            }

            is EventCreateUpdateScreenEvent.UpdateImage -> {
                _stateEventImage.update {
                    it.copy(image = event.image)
                }
            }

            is EventCreateUpdateScreenEvent.UpdateTitle -> {
                if (!stateIsUpdate.value) {
                    _stateEvent.update {
                        it.copy(title = event.title)
                    }
                }
            }

            is EventCreateUpdateScreenEvent.UpdateDescription -> {
                _stateEvent.update {
                    it.copy(description = event.description)
                }
            }

            is EventCreateUpdateScreenEvent.UpdateCapacity -> {
                _stateEvent.update {
                    it.copy(capacity = event.capacity)
                }
            }

            is EventCreateUpdateScreenEvent.UpdateDate -> {
                _stateEvent.update {
                    it.copy(date = event.date)
                }
            }

            is EventCreateUpdateScreenEvent.UpdateTime -> {
                _stateEvent.update {
                    it.copy(time = event.time)
                }
            }

            is EventCreateUpdateScreenEvent.UpdateSearchLocation -> {
                _searchLocation.update { event.searchLocation }
            }

            is EventCreateUpdateScreenEvent.UpdateLocation -> {
                _stateEvent.update {
                    it.copy(placeId = event.locationId)
                }
            }

            is EventCreateUpdateScreenEvent.UpdateProvidedTools -> {
                _stateEvent.update {
                    it.copy(providedTools = event.providedTools)
                }
            }

            is EventCreateUpdateScreenEvent.UpdateRequiredTools -> {
                _stateEvent.update {
                    it.copy(requiredTools = event.requiredTools)
                }
            }

            is EventCreateUpdateScreenEvent.UpdateSearchCategory -> {
                _searchCategory.update { event.searchCategory }
            }

            is EventCreateUpdateScreenEvent.AddCategory -> {
                val contains = _stateEvent.value.categoryList.find {
                    it.id == event.category.id
                }
                if (contains == null) {
                    _stateEvent.value.categoryList.add(event.category)
                    _stateCategoriesSize.value = _stateEvent.value.categoryList.size
                    _stateEvent.update {
                        it.copy(categoryList = _stateEvent.value.categoryList)
                    }
                }
            }

            is EventCreateUpdateScreenEvent.RemoveCategory -> {
                _stateEvent.value.categoryList.removeAt(event.index)
                _stateCategoriesSize.value = _stateEvent.value.categoryList.size
                _stateEvent.update {
                    it.copy(categoryList = _stateEvent.value.categoryList)
                }
            }

            is EventCreateUpdateScreenEvent.UpdateSalaryAmount -> {
                _stateEvent.update {
                    it.copy(salaryAmount = event.salaryAmount)
                }
            }

            is EventCreateUpdateScreenEvent.UpdateSalaryGoodTitle -> {
                _stateEvent.update {
                    it.copy(salaryGoodTitle = event.salaryGoodTitle)
                }
            }

            is EventCreateUpdateScreenEvent.UpdateSalaryType -> {
                _stateEvent.update {
                    it.copy(salaryType = event.salaryType)
                }
            }

            is EventCreateUpdateScreenEvent.UpdateSalaryUnit -> {
                _stateEvent.update {
                    it.copy(
                        salaryUnit = event.salaryUnit
                    )
                }
            }

            is EventCreateUpdateScreenEvent.OnCreateEventButtonClick -> {
                validateInput(_stateEvent.value)

                if (_stateEventCreateUpdate.value.error == null) {
                    if (_stateEventImage.value.imageUrl != null) {
                        uploadImage(_stateEventImage.value.image!!)

                        _stateEvent.value = _stateEvent.value.copy(
                            imageUrl = _stateEventImage.value.imageUrl!!
                        )
                    }

                    _stateEventCreateUpdate.update {
                        it.copy(event = _stateEvent.value.toEvent(_stateEvent.value))
                    }

                    if (_stateEventCreateUpdate.value.event != null) {
                        createEvent(_stateEventCreateUpdate.value.event!!)
                    }
                }
            }

            is EventCreateUpdateScreenEvent.OnUpdateEventButtonClick -> {
                validateInput(_stateEvent.value)

                if (_stateEventCreateUpdate.value.error == null) {
                    if (_stateEventImage.value.imageUrl != null) {
                        uploadImage(_stateEventImage.value.image!!)

                        _stateEvent.value = _stateEvent.value.copy(
                            imageUrl = _stateEventImage.value.imageUrl!!
                        )
                    }

                    _stateEventCreateUpdate.update {
                        it.copy(event = _stateEvent.value.toEvent(_stateEvent.value))
                    }

                    if (_stateEventCreateUpdate.value.event != null) {
                        updateEvent(_stateEventCreateUpdate.value.event!!, _eventId.value)
                    }
                }
            }

            is EventCreateUpdateScreenEvent.RemoveError -> {
                _stateEventCreateUpdate.update {
                    it.copy(error = null)
                }
            }
        }
    }

    private fun uploadImage(image: ByteArray) {
        coroutineScope().launch {
            uploadImageUseCase(image).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _stateEventImage.value = _stateEventImage.value.copy(
                            imageUrl = result.data.imageURL
                        )
                    }

                    is ResultHandler.Error -> {
                        _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                            error = result.error.asUiText().asNonCompString(),
                            isLoading = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    private fun createEvent(event: EventCreateUpdateDto) {
        coroutineScope().launch {
            createEventUseCase(event).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        onNavigateToDetailScreen(result.data.eventId)
                    }

                    is ResultHandler.Error -> {
                        _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                            error = result.error.asUiText().asNonCompString(),
                            isLoading = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    private fun updateEvent(event: EventCreateUpdateDto, eventId: String) {
        coroutineScope().launch {
            updateEventUseCase(event, eventId).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        onNavigateToDetailScreen(eventId)
                    }

                    is ResultHandler.Error -> {
                        _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                            error = result.error.asUiText().asNonCompString(),
                            isLoading = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun getCategories() {
        coroutineScope().launch {
            getAllCategoriesUseCase().collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _categories.value = result.data.categories.sortedBy { it.name }
                    }

                    is ResultHandler.Error -> {
                        _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                            error = result.error.asUiText().asNonCompString(),
                            isLoading = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    private fun getPlaces(query: String) {
        coroutineScope().launch {
            getPlacesUseCase(query).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _locations.value = result.data.items
                    }

                    is ResultHandler.Error -> {
                        _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                            error = result.error.asUiText().asNonCompString(),
                            isLoading = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    private fun validateInput(event: EventState) {
        coroutineScope().launch {
            createUpdateEventValidation.validateInput(event).collect { result ->
                if (result is ResultHandler.Error) {
                    _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                        error = result.error.asUiText().asNonCompString()
                    )
                } else if (result is ResultHandler.Success) {
                    _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                        error = null
                    )
                }
            }
        }
    }
}