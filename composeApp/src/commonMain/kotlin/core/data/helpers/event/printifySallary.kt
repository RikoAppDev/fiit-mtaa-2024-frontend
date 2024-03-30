import androidx.compose.runtime.Composable
import core.data.helpers.round
import core.domain.event.SallaryType
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_card__per_hour_shortcut
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

data class SallaryObject (
    val sallaryType: SallaryType,
    val sallaryAmount: Double,
    val sallaryProductName: String?,
    val sallaryUnit: String?,
)

@Composable
@OptIn(ExperimentalResourceApi::class)
fun printifySallary(sallary:SallaryObject ): String {
    val sallaryType = sallary.sallaryType
    val amount = round(sallary.sallaryAmount, 3)

    var ret = ""
    val hourShortcut = stringResource(Res.string.event_card__per_hour_shortcut)
    if (sallaryType == SallaryType.MONEY) {
        ret = "$amount €"
    } else {
        ret = "${sallary.sallaryProductName} - $amount ${sallary.sallaryUnit}"
    }
    return "$ret / $hourShortcut"
}