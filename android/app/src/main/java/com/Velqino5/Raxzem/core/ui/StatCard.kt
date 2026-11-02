package com.Velqino5.Raxzem.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Velqino5.Raxzem.core.theme.PiggySurfaceRaised
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextSecondary

@Composable
fun StatCard(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier,
    announce: Boolean = false
) {
    val shape = RoundedCornerShape(14.dp)
    val semanticsModifier = if (announce) {
        Modifier.semantics { liveRegion = LiveRegionMode.Polite }
    } else {
        Modifier
    }
    Surface(
        shape = shape,
        color = PiggySurfaceRaised.copy(alpha = 0.82f),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.3f)),
        tonalElevation = 0.dp,
        shadowElevation = 6.dp,
        modifier = modifier.then(semanticsModifier)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(PiggyTextPrimary.copy(alpha = 0.07f), Color.Transparent)
                    )
                )
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(accent)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = accent,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = PiggyTextSecondary,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}
