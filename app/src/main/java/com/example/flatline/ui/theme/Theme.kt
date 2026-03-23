package com.example.flatline.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80,

    background = Color(0xFF1C1408),       // richer, more amber-brown
    onBackground = Color(0xFFF5ECD8),     // warm ivory
    surface = Color(0xFF2A1F0E),          // noticeably warmer than background
    onSurface = Color(0xFFF5ECD8),
    surfaceVariant = Color(0xFF3D2E18),   // visible warm brown for fields/chips
    onSurfaceVariant = Color(0xFFBFA880), // golden-tan muted text
    primary = Color(0xFFE8935A),
    onPrimary = Color(0xFF1C1408),
)

private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40,

    background = Color(0xFFFFF8EE),       // warm amber-tinted white
    onBackground = Color(0xFF1C1408),
    surface = Color(0xFFFFF0D6),          // noticeably amber, not plain white
    onSurface = Color(0xFF1C1408),
    surfaceVariant = Color(0xFFEDD9B0),   // golden for fields/chips
    onSurfaceVariant = Color(0xFF7A5C35),
    primary = Color(0xFFD4743F),
    onPrimary = Color(0xFFFFFFFF),
    error = Color(0xFF610701),
    onError = Color(0xFFFFFFFF),


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun FlatlineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}