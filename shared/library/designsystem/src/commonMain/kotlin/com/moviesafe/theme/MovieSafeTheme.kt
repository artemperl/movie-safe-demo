package com.moviesafe.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Palette ──────────────────────────────────────────────────────────────────

private object Palette {
    // Blue family
    val Blue10 = Color(0xFF001E2C)
    val Blue20 = Color(0xFF003549)
    val Blue40 = Color(0xFF006590)
    val Blue80 = Color(0xFF72D1FF)
    val Blue90 = Color(0xFFC4E7FF)

    // Amber family (accent / "now playing" glow)
    val Amber10 = Color(0xFF261A00)
    val Amber20 = Color(0xFF3F2C00)
    val Amber80 = Color(0xFFFFBA00)
    val Amber90 = Color(0xFFFFDF98)

    // Neutral
    val Neutral10 = Color(0xFF1A1C1E)
    val Neutral20 = Color(0xFF2F3033)
    val Neutral90 = Color(0xFFE2E2E6)
    val Neutral95 = Color(0xFFF1F0F4)
    val Neutral99 = Color(0xFFFAFCFF)

    // Neutral variant
    val NeutralVariant20 = Color(0xFF2A2D31)
    val NeutralVariant30 = Color(0xFF40454C)
    val NeutralVariant50 = Color(0xFF70757D)
    val NeutralVariant80 = Color(0xFFC0C7D0)
    val NeutralVariant90 = Color(0xFFDDE3EC)

    // Error
    val Red10 = Color(0xFF410002)
    val Red80 = Color(0xFFFFB4AB)
    val Red90 = Color(0xFFFFDAD6)
    val RedError = Color(0xFFBA1A1A)
}

// ── Color schemes ─────────────────────────────────────────────────────────────

private val LightColorScheme = lightColorScheme(
    primary = Palette.Blue40,
    onPrimary = Color.White,
    primaryContainer = Palette.Blue90,
    onPrimaryContainer = Palette.Blue10,

    secondary = Color(0xFF795900),
    onSecondary = Color.White,
    secondaryContainer = Palette.Amber90,
    onSecondaryContainer = Palette.Amber10,

    tertiary = Color(0xFF6B3F82),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF2DAFF),
    onTertiaryContainer = Color(0xFF25005A),

    error = Palette.RedError,
    onError = Color.White,
    errorContainer = Palette.Red90,
    onErrorContainer = Palette.Red10,

    background = Palette.Neutral99,
    onBackground = Palette.Neutral10,
    surface = Palette.Neutral99,
    onSurface = Palette.Neutral10,
    surfaceVariant = Palette.NeutralVariant90,
    onSurfaceVariant = Palette.NeutralVariant30,

    outline = Palette.NeutralVariant50,
    scrim = Color.Black,
    inverseSurface = Palette.Neutral20,
    inverseOnSurface = Palette.Neutral95,
    inversePrimary = Palette.Blue80,
)

private val DarkColorScheme = darkColorScheme(
    primary = Palette.Blue80,
    onPrimary = Palette.Blue20,
    primaryContainer = Color(0xFF004D68),
    onPrimaryContainer = Palette.Blue90,

    secondary = Palette.Amber80,
    onSecondary = Palette.Amber20,
    secondaryContainer = Color(0xFF574000),
    onSecondaryContainer = Palette.Amber90,

    tertiary = Color(0xFFDFB4F9),
    onTertiary = Color(0xFF3D0060),
    tertiaryContainer = Color(0xFF560079),
    onTertiaryContainer = Color(0xFFF2DAFF),

    error = Palette.Red80,
    onError = Palette.Red10,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Palette.Red90,

    background = Color(0xFF0F1113),
    onBackground = Palette.Neutral90,
    surface = Color(0xFF0F1113),
    onSurface = Palette.Neutral90,
    surfaceVariant = Palette.NeutralVariant20,
    onSurfaceVariant = Palette.NeutralVariant80,

    outline = Palette.NeutralVariant50,
    scrim = Color.Black,
    inverseSurface = Palette.Neutral90,
    inverseOnSurface = Palette.Neutral10,
    inversePrimary = Palette.Blue40,
)

// ── Theme ─────────────────────────────────────────────────────────────────────

@Composable
fun MovieSafeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MovieSafeTypography,
        content = content,
    )
}
