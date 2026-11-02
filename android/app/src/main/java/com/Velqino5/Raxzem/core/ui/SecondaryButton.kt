package com.Velqino5.Raxzem.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Velqino5.Raxzem.core.theme.PiggyBorder
import com.Velqino5.Raxzem.core.theme.PiggySurfaceRaised
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary

@Composable
fun SecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    iconPainter: Painter? = null,
    enabled: Boolean = true,
    height: Dp = 52.dp,
    contentColor: Color = PiggyTextPrimary,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, PiggyBorder.copy(alpha = if (enabled) 0.4f else 0.18f)),
        contentPadding = PaddingValues(horizontal = 18.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = PiggySurfaceRaised.copy(alpha = 0.85f),
            contentColor = contentColor,
            disabledContainerColor = PiggySurfaceRaised.copy(alpha = 0.4f),
            disabledContentColor = contentColor.copy(alpha = 0.45f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        if (iconPainter != null) {
            Image(
                painter = iconPainter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
