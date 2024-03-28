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
import grabit.composeapp.generated.resources.Nunito_Light
import grabit.composeapp.generated.resources.Nunito_Regular
import grabit.composeapp.generated.resources.Nunito_SemiBold
import grabit.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font


@OptIn(ExperimentalResourceApi::class)
@Composable
private fun NunitoFont() = FontFamily(
    Font(Res.font.Nunito_Regular, FontWeight.Normal, FontStyle.Normal),
    Font(Res.font.Nunito_Bold, FontWeight.Bold, FontStyle.Normal),
    Font(Res.font.Nunito_SemiBold, FontWeight.SemiBold, FontStyle.Normal),
    Font(Res.font.Nunito_Light, FontWeight.Light, FontStyle.Normal),
)

@Composable
fun CustomTypography() = Typography(
    body1 = TextStyle(
        fontFamily = NunitoFont(),
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
    ),
    body2 = TextStyle(
        fontFamily = NunitoFont(),
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = NunitoFont(),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    caption = TextStyle(
        fontFamily = NunitoFont(),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    h1 = TextStyle(
        fontFamily = NunitoFont(),
        fontSize = 28.sp,
        fontWeight = FontWeight(700),
        color = TextH,
        letterSpacing = 0.08.sp,
    ),
    h2 = TextStyle(
        fontFamily = NunitoFont(),
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextH,
    ),
    h3 = TextStyle(
        fontFamily = NunitoFont(),
        fontSize = 18.sp,
        fontWeight = FontWeight(600),
        color = Color.Black,
    ),
)


val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Light,
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
        fontFamily = FontFamily.Default,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextH,
    ),
    h3 = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight(600),
        color = Color.Black,
    ),
)
