package com.Velqino5.Raxzem.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Velqino5.Raxzem.core.theme.PiggyBorder
import com.Velqino5.Raxzem.core.theme.PiggySurface
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary

@Composable
fun GlassPanelSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    fill: Color = PiggySurface.copy(alpha = 0.95f),
    borderColor: Color = PiggyBorder.copy(alpha = 0.3f),
    elevation: Dp = 14.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        shape = shape,
        color = fill,
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = elevation,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(PiggyTextPrimary.copy(alpha = 0.07f), Color.Transparent)
                    )
                )
                .padding(contentPadding),
            content = content
        )
    }
}
