package core.data.helpers.event

import SallaryObject
import androidx.compose.runtime.Composable
import core.data.helpers.round
import core.domain.event.SallaryType
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_card__per_hour_shortcut
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalResourceApi::class)
fun printifyExpense(sallary: SallaryObject, hours: Long): String {
    val sallaryType = sallary.sallaryType
    val amount = round(sallary.sallaryAmount, 3)

    var ret = ""
    val hourShortcut = stringResource(Res.string.event_card__per_hour_shortcut)
    if (sallaryType == SallaryType.MONEY) {
        ret = "${round(amount * hours, 2)} €"
    } else {
        ret = "${sallary.sallaryProductName} - ${round(amount * hours, 2)} ${sallary.sallaryUnit}"
    }
    return ret
}
