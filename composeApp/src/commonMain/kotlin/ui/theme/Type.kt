package ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import grabit.composeapp.generated.resources.Nunito_Bold
import grabit.composeapp.generated.resources.Nunito_Regular
import grabit.composeapp.generated.resources.Nunito_SemiBold
import grabit.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

@OptIn(ExperimentalResourceApi::class)
@Composable
fun getTypography(): Typography {
    val nunitoRegular = FontFamily(
        Font(Res.font.Nunito_Regular, FontWeight.Normal, FontStyle.Normal)
    )
    val nunitoSemiBold = FontFamily(
        Font(Res.font.Nunito_SemiBold, FontWeight.Normal, FontStyle.Normal)
    )
    val nunitoBold = FontFamily(
        Font(Res.font.Nunito_Bold, FontWeight.Normal, FontStyle.Normal)
    )

    return Typography(
        body1 = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
        ),
        body2 = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        button = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        ),
        caption = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        ),
        h1 = TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight(700),
            color = TextH,
            letterSpacing = 0.08.sp,
        ),
        h2 = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight(600),
            color = TextH,
        ),
        h3 = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight(700),
            color = Color.Black,
        )
    )
}