package event.domain

import core.domain.ResultHandler
import event.presentation.create_update.EventState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class CreateUpdateEventValidation {
    suspend fun validateInput(
        event: EventState,
        update: Boolean
    ): Flow<ResultHandler<Boolean, CreateUpdateFormError.MissingFieldError>> =
        flow {
            if (event.title.isBlank()) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.TITLE))
            } else if (event.capacity.isBlank()) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.CAPACITY))
            } else if (event.date == null) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.DATE))
            } else if (event.time == null) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.TIME))
            } else if (event.date.toEpochDays() == Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date.toEpochDays() &&
                event.time.toSecondOfDay() < Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()).time.toSecondOfDay()
            ) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.DATE_TIME_PAST))
            } else if (!update && event.placeId == null) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.LOCATION))
            } else if (event.salaryAmount.isBlank()) {
                emit(ResultHandler.Error(CreateUpdateFormError.MissingFieldError.SALARY_AMOUNT))
            } else {
                emit(ResultHandler.Success(true))
            }
        }
}