package core.presentation.error_string_mapper

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

sealed class UiErrorText {
    data class DynamicString(val value: String) : UiErrorText()
    class StringRes @OptIn(ExperimentalResourceApi::class) constructor(
        val res: StringResource,
        val args: Array<Any> = arrayOf()
    ) : UiErrorText()

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun asCompString(): String {
        return when (this) {
            is DynamicString -> value
            is StringRes -> stringResource(res, *args)
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    suspend fun asNonCompString(): String {
        return when (this) {
            is DynamicString -> value
            is StringRes -> getString(res, *args)
        }
    }
}