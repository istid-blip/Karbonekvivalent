package com.sveis.karbonekvivalent.uiKE

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import karbonekvivalent.shared.generated.resources.Res
import karbonekvivalent.shared.generated.resources.geist_mono_thin
import karbonekvivalent.shared.generated.resources.geist_mono_regular
import karbonekvivalent.shared.generated.resources.geist_mono_medium
import karbonekvivalent.shared.generated.resources.geist_mono_bold
import karbonekvivalent.shared.generated.resources.geist_medium
import org.jetbrains.compose.resources.Font

enum class AppThemeType {
    FIN,
    RETRO
}

@Composable
fun geistMonoFontFamily() = FontFamily(
    Font(Res.font.geist_mono_thin, FontWeight.Thin),
    Font(Res.font.geist_mono_regular, FontWeight.Normal),
    Font(Res.font.geist_mono_medium, FontWeight.Medium),
    Font(Res.font.geist_mono_bold, FontWeight.Bold)
)

val LocalUseEdgeToEdge = staticCompositionLocalOf { true }

// Egne stiler for TallVelger-komponenten
data class TallVelgerStyle(
    val glassGjenskinnFarge: Color,
    val glassAlphaSenter: Float,
    val glassAlphaKanter: Float,
    val glassRammeAlpha: Float,
    val indikatorAlpha: Float
)

val FinTallVelgerStyle = TallVelgerStyle(
    glassGjenskinnFarge = Color.White,
    glassAlphaSenter = 0.04f,
    glassAlphaKanter = 0.01f,
    glassRammeAlpha = 0.05f,
    indikatorAlpha = 0.9f
)

val RetroTallVelgerStyle = TallVelgerStyle(
    glassGjenskinnFarge = Color(0xFF00FF00),
    glassAlphaSenter = 0.10f,
    glassAlphaKanter = 0.04f,
    glassRammeAlpha = 0.2f,
    indikatorAlpha = 1.0f
)

val LocalTallVelgerStyle = staticCompositionLocalOf { FinTallVelgerStyle }
val LocalAppTheme = staticCompositionLocalOf { AppThemeType.FIN }

object HeatInputTheme {
    val tallVelgerStyle: TallVelgerStyle
        @Composable
        @ReadOnlyComposable
        get() = LocalTallVelgerStyle.current

    val current: AppThemeType
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTheme.current
}

private val FinFarger = darkColorScheme(
    primary = Color(0xFFFF3B30),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFF3B30).copy(alpha = 0.2f),
    onPrimaryContainer = Color(0xFFFF3B30),
    secondary = Color(0xFF8B0000),
    onSecondary = Color.White,
    background = Color.Black,
    surface = Color(0xFF1C1C1E),
    surfaceVariant = Color(0xFF151516), // Ny farge for formelfelt og lignende
    onBackground = Color.White,
    onSurface = Color.White,
    outline = Color(0xFFFF3B30).copy(alpha = 0.5f),
    outlineVariant = Color(0xFF3A3A3C)
)

private val RetroFarger = darkColorScheme(
    primary = Color(0xFF33FF4D),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF004400),
    onPrimaryContainer = Color(0xFF33FF4D),
    secondary = Color(0xFF33FF4D), // Gjort lysere for bedre lesbarhet som tekst
    onSecondary = Color.Black,
    background = Color.Black,
    surface = Color.Black,
    surfaceVariant = Color(0xFF001A00), // Mørk grønnaktig for retro formelfelt
    onBackground = Color(0xFF33FF4D),
    onSurface = Color(0xFF33FF4D),
    outline = Color(0xFF33FF4D),
    outlineVariant = Color(0xFF004400)
)


@Composable
fun getFinTypography(): Typography {
    return Typography(
        headlineLarge = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        ),
        titleMedium = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        ),
        labelSmall = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
    )
}


// Bruker den eksisterende geistMonoFontFamily() definisjonen
@Composable
private fun getRetroTypography(): Typography {
    val geistMono = geistMonoFontFamily()
    return Typography(
        displayLarge = TextStyle(fontFamily = geistMono, fontWeight = FontWeight.Black, fontSize = 57.sp),
        headlineLarge = TextStyle(fontFamily = geistMono, fontWeight = FontWeight.Bold, fontSize = 30.sp),
        headlineSmall = TextStyle(fontFamily = geistMono, fontWeight = FontWeight.Bold, fontSize = 20.sp),
        titleLarge = TextStyle(fontFamily = geistMono, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp),
        titleMedium = TextStyle(fontFamily = geistMono, fontWeight = FontWeight.Bold, fontSize = 18.sp),
        bodyLarge = TextStyle(fontFamily = geistMono, fontWeight = FontWeight.Normal, fontSize = 16.sp),
        bodyMedium = TextStyle(fontFamily = geistMono, fontWeight = FontWeight.Normal, fontSize = 14.sp),
        labelSmall = TextStyle(fontFamily = geistMono, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    )
}

private val FinShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp)
)

private val RetroShapes = Shapes(
    extraSmall = RoundedCornerShape(0.dp),
    small = RoundedCornerShape(0.dp),
    medium = RoundedCornerShape(0.dp)
)

@Composable
fun CrtEffektOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val scanlineSpacing = 4.dp.toPx()
        for (y in 0 until size.height.toInt() step scanlineSpacing.toInt()) {
            drawLine(
                color = Color.Black.copy(alpha = 0.05f),
                start = Offset(0f, y.toFloat()),
                end = Offset(size.width, y.toFloat()),
                strokeWidth = 2.dp.toPx()
            )
        }
    }
}

@Composable
fun AppTheme(
    valgtTema: AppThemeType = AppThemeType.RETRO,
    useEdgeToEdge: Boolean = true,
    darkTheme: Boolean = true, // Added to match App.kt usage
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val tema = if (darkTheme) valgtTema else AppThemeType.FIN
    val colorScheme = if (tema == AppThemeType.FIN) FinFarger else RetroFarger
    val shapes = if (tema == AppThemeType.FIN) FinShapes else RetroShapes
    val typography = if (tema == AppThemeType.FIN) getFinTypography() else getRetroTypography()
    val tallVelgerStyle = if (tema == AppThemeType.FIN) FinTallVelgerStyle else RetroTallVelgerStyle

    CompositionLocalProvider(
        LocalTallVelgerStyle provides tallVelgerStyle,
        LocalAppTheme provides tema,
        LocalUseEdgeToEdge provides useEdgeToEdge
    ) {
        MaterialTheme(colorScheme = colorScheme, shapes = shapes, typography = typography) {
            Box(modifier = modifier) {
                content()
                if (tema == AppThemeType.RETRO) CrtEffektOverlay(Modifier.matchParentSize())
            }
        }
    }
}
