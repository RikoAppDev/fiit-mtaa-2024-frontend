package event.domain

import core.domain.Error

sealed interface CreateUpdateFormError : Error {
    enum class MissingFieldError : CreateUpdateFormError {
        TITLE, CAPACITY, DATE, TIME, DATE_TIME_PAST, LOCATION, SALARY_AMOUNT
    }
}