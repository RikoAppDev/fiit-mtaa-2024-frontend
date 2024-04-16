package core.presentation.error_string_mapper

import auth.domain.AuthError
import auth.domain.AuthError.*
import core.domain.DataError
import core.domain.DataError.*
import core.domain.Error
import event.domain.CreateUpdateFormError
import event.domain.CreateUpdateFormError.*
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.error__bad_request
import grabit.composeapp.generated.resources.error__client
import grabit.composeapp.generated.resources.error__conflict
import grabit.composeapp.generated.resources.error__email_invalid_format
import grabit.composeapp.generated.resources.error__forbidden
import grabit.composeapp.generated.resources.error__no_internet
import grabit.composeapp.generated.resources.error__no_record_found
import grabit.composeapp.generated.resources.error__not_found
import grabit.composeapp.generated.resources.error__not_logged_in
import grabit.composeapp.generated.resources.error__password_no_digit
import grabit.composeapp.generated.resources.error__password_no_match
import grabit.composeapp.generated.resources.error__password_too_short
import grabit.composeapp.generated.resources.error__payload_too_large
import grabit.composeapp.generated.resources.error__request_timeout
import grabit.composeapp.generated.resources.error__response
import grabit.composeapp.generated.resources.error__serialization
import grabit.composeapp.generated.resources.error__server
import grabit.composeapp.generated.resources.error__token_expired
import grabit.composeapp.generated.resources.error__too_many_requests
import grabit.composeapp.generated.resources.error__unauthorised
import grabit.composeapp.generated.resources.error__unknown
import grabit.composeapp.generated.resources.event_create_update_screen__capacity_error
import grabit.composeapp.generated.resources.event_create_update_screen__date_error
import grabit.composeapp.generated.resources.event_create_update_screen__location_error
import grabit.composeapp.generated.resources.event_create_update_screen__salary_amount_error
import grabit.composeapp.generated.resources.event_create_update_screen__time_error
import grabit.composeapp.generated.resources.event_create_update_screen__title_error
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
fun Error.asUiText(): UiErrorText {
    return when (this) {
        is DataError -> {
            when (this) {
                is NetworkError -> {
                    when (this) {
                        NetworkError.RESPONSE -> UiErrorText.StringRes(
                            Res.string.error__response
                        )

                        NetworkError.BAD_REQUEST -> UiErrorText.StringRes(
                            Res.string.error__bad_request
                        )

                        NetworkError.UNAUTHORISED -> UiErrorText.StringRes(
                            Res.string.error__unauthorised
                        )

                        NetworkError.FORBIDDEN -> UiErrorText.StringRes(
                            Res.string.error__forbidden
                        )

                        NetworkError.NOT_FOUND -> UiErrorText.StringRes(
                            Res.string.error__not_found
                        )

                        NetworkError.REQUEST_TIMEOUT -> UiErrorText.StringRes(
                            Res.string.error__request_timeout
                        )

                        NetworkError.CONFLICT -> UiErrorText.StringRes(
                            Res.string.error__conflict
                        )

                        NetworkError.PAYLOAD_TOO_LARGE -> UiErrorText.StringRes(
                            Res.string.error__payload_too_large
                        )

                        NetworkError.TOO_MANY_REQUESTS -> UiErrorText.StringRes(
                            Res.string.error__too_many_requests
                        )

                        NetworkError.CLIENT -> UiErrorText.StringRes(
                            Res.string.error__client
                        )

                        NetworkError.SERVER -> UiErrorText.StringRes(
                            Res.string.error__server
                        )

                        NetworkError.SERIALIZATION -> UiErrorText.StringRes(
                            Res.string.error__serialization
                        )

                        NetworkError.NO_INTERNET -> UiErrorText.StringRes(
                            Res.string.error__no_internet
                        )

                        NetworkError.UNKNOWN -> UiErrorText.StringRes(
                            Res.string.error__unknown
                        )
                    }
                }

                is LocalError -> {
                    when (this) {
                        LocalError.NO_RECORD -> UiErrorText.StringRes(
                            Res.string.error__no_record_found
                        )

                        LocalError.NOT_LOGGED_IN -> UiErrorText.StringRes(
                            Res.string.error__not_logged_in
                        )
                    }
                }
            }
        }

        is AuthError -> {
            when (this) {
                is EmailError -> {
                    when (this) {
                        EmailError.INVALID_FORMAT -> UiErrorText.StringRes(
                            Res.string.error__email_invalid_format
                        )
                    }
                }

                is PasswordError -> {
                    when (this) {
                        PasswordError.TOO_SHORT -> UiErrorText.StringRes(
                            Res.string.error__password_too_short
                        )

                        PasswordError.NO_DIGIT -> UiErrorText.StringRes(
                            Res.string.error__password_no_digit
                        )

                        PasswordError.NO_MATCH -> UiErrorText.StringRes(
                            Res.string.error__password_no_match
                        )
                    }
                }

                is TokenError -> {
                    when (this) {
                        TokenError.EXPIRED -> UiErrorText.StringRes(
                            Res.string.error__token_expired
                        )
                    }
                }
            }
        }

        is CreateUpdateFormError -> {
            when (this) {
                is MissingFieldError -> {
                    when (this) {
                        MissingFieldError.TITLE -> UiErrorText.StringRes(
                            Res.string.event_create_update_screen__title_error
                        )

                        MissingFieldError.CAPACITY -> UiErrorText.StringRes(
                            Res.string.event_create_update_screen__capacity_error
                        )

                        MissingFieldError.DATE -> UiErrorText.StringRes(
                            Res.string.event_create_update_screen__date_error
                        )

                        MissingFieldError.TIME -> UiErrorText.StringRes(
                            Res.string.event_create_update_screen__time_error
                        )

                        MissingFieldError.LOCATION -> UiErrorText.StringRes(
                            Res.string.event_create_update_screen__location_error
                        )

                        MissingFieldError.SALARY_AMOUNT -> UiErrorText.StringRes(
                            Res.string.event_create_update_screen__salary_amount_error
                        )
                    }
                }
            }
        }

        else -> UiErrorText.StringRes(Res.string.error__unknown)
    }
}

