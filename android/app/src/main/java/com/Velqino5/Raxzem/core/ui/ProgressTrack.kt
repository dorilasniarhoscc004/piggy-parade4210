package com.Velqino5.Raxzem.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.Velqino5.Raxzem.core.theme.PiggyAccent
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary

@Composable
fun ProgressTrack(
    filled: Int,
    total: Int,
    modifier: Modifier = Modifier,
    accent: Color = PiggyAccent
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        repeat(total.coerceAtLeast(1)) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        if (index < filled) accent else PiggyTextPrimary.copy(alpha = 0.12f)
                    )
            )
        }
    }
}
