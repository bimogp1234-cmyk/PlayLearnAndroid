package com.example.myapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PL_Green,
    secondary = PL_Blue,
    tertiary = PL_Gold,
    background = Color(0xFF1F2933),
    surface = Color(0xFF111827),
    onPrimary = White,
    onBackground = White,
    onSurface = White,
    error = PL_Red
)

private val LightColorScheme = lightColorScheme(
    primary = PL_Green,
    secondary = PL_Blue,
    tertiary = PL_Gold,
    background = PL_Cream,
    surface = White,
    onPrimary = White,
    onSecondary = PL_Ink,
    onBackground = PL_Ink,
    onSurface = PL_Ink,
    error = PL_Red,
    outline = PL_Line,
    secondaryContainer = PL_GreenSoft
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
