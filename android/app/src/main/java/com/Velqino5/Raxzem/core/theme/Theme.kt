package com.Velqino5.Raxzem.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PiggyColorScheme = darkColorScheme(
    primary = PiggyPrimary,
    onPrimary = PiggyOnPrimary,
    primaryContainer = PiggyPrimaryDeep,
    onPrimaryContainer = PiggyTextPrimary,
    secondary = PiggyAccent,
    onSecondary = PiggyOnPrimary,
    secondaryContainer = PiggySurfaceRaised,
    onSecondaryContainer = PiggyTextPrimary,
    tertiary = PiggyLeaf,
    onTertiary = PiggyOnPrimary,
    background = PiggyBgTop,
    onBackground = PiggyTextPrimary,
    surface = PiggySurface,
    onSurface = PiggyTextPrimary,
    surfaceVariant = PiggySurfaceRaised,
    onSurfaceVariant = PiggyTextSecondary,
    outline = PiggyBorder,
    outlineVariant = PiggyTextMuted,
    error = PiggyDanger,
    onError = PiggyTextPrimary,
    scrim = PiggyBgDeep
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PiggyColorScheme,
        typography = AppTypography,
        content = content
    )
}
