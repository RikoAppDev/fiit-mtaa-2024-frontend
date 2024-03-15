package ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import grabit.composeapp.generated.resources.Nunito_Black
import grabit.composeapp.generated.resources.Nunito_ExtraBold
import grabit.composeapp.generated.resources.Nunito_Light
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.nunito_regular
import org.jetbrains.compose.resources.ExperimentalResourceApi

val Typography = Typography(
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
        color = Color(0xFF19191B),
        letterSpacing = 0.08.sp,
    ),

    h2 = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight(600),
        color = Color(0xFF19191B),

    ),
    h3 = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight(700),
        color = Color.Black,

    )
)