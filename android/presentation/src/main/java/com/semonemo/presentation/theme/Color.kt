package com.semonemo.presentation.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Red = Color(0xFFFF0000)
val Blue1 = Color(0xFF8398A2)
val Blue2 = Color(0xFF526788)
val Blue3 = Color(0xFF267DBC)
val GunMetal = Color(0xFF252A34)
val White = Color(0xFFFFFFFF)
val Gray01 = Color(0xFF555555)
val Gray02 = Color(0xFF949494)
val Gray03 = Color(0xFFD9D9D9)
val WhiteGray = Color(0xFFE7E7E7)

val ProgressRed = Color(0xFFFF6961)
val ProgressYellow = Color(0xFFFDFD96)
val ProgressGreen = Color(0xFF77DD77)

val FramePink = Color(0xFFFFCCCD)
val FrameOrange = Color(0xFFFFD085)
val FrameBlue = Color(0xFFADC4FF)
val FramePurple = Color(0xFFFFB3FF)
val BlackGradient =
    Brush.verticalGradient(
        colors =
            listOf(
                Color(0xFFB5B5B5),
                Color.Black,
            ),
    )
val PinkGradient =
    Brush.verticalGradient(
        colors =
            listOf(
                Color(0xFFFFE5E5),
                Color(0xFFFF9393),
            ),
    )
val GreenGradient =
    Brush.verticalGradient(
        colors =
            listOf(
                Color(0xFFA7FF85),
                Color(0xFFFFEA4D),
            ),
    )
val PurpleGradient =
    Brush.verticalGradient(
        colors =
            listOf(
                Color(0xFFFFD085),
                Color(0xFF9C33FF),
            ),
    )
val BlueGradient =
    Brush.verticalGradient(
        colors =
            listOf(
                Color(0xFFADC4FF),
                Color(0xFF2664FF),
            ),
    )
val Rainbow =
    Brush.verticalGradient(
        colors =
            listOf(
                Color(0xFFFB3DFB),
                Color(0xFFFF4F64),
                Color(0xFFFAE94E),
                Color(0xFF71F74F),
                Color(0xFF3887FE),
                Color(0xFF812CED),
            ),
    )
val Main01 =
    Brush.linearGradient(
        colors =
            listOf(
                White,
                Color(0xFFEBF0FF),
            ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f),
    )
val Main02 =
    Brush.horizontalGradient(
        colors =
            listOf(
                Color(0xFF535353),
                Color(0xFF273B71),
            ),
        startX = 0f,
        endX = Float.POSITIVE_INFINITY,
    )
