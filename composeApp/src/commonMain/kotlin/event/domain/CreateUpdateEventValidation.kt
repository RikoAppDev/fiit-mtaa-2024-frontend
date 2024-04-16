package event.domain

import core.domain.ResultHandler
import event.presentation.create_update.EventState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CreateUpdateEventValidation {
    suspend fun validateInput(event: EventState): Flow<ResultHandler<Boolean, CreateUpdateFormError.MissingFieldError>> =
        flow {
            if (event.title.isBlank()) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.TITLE))
            } else if (event.capacity.isBlank()) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.CAPACITY))
            } else if (event.date == null) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.DATE))
            } else if (event.time == null) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.TIME))
            } else if (event.placeId.isBlank()) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.LOCATION))
            } else if (event.salaryAmount.isBlank()) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.SALARY_AMOUNT))
            } else {
                emit(ResultHandler.Success(true))
            }
        }
}