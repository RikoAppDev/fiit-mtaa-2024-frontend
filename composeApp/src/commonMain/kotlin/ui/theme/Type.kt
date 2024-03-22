package ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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
