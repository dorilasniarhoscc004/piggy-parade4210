package com.Velqino5.Raxzem.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.Velqino5.Raxzem.R
import com.Velqino5.Raxzem.core.theme.PiggyAccent
import com.Velqino5.Raxzem.core.theme.PiggySurface
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextSecondary

@Composable
fun PausedDialog(onResume: () -> Unit, onQuit: () -> Unit) {
    AlertDialog(
        onDismissRequest = onResume,
        shape = RoundedCornerShape(20.dp),
        containerColor = PiggySurface,
        titleContentColor = PiggyTextPrimary,
        textContentColor = PiggyTextSecondary,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        ),
        title = {
            Text(
                text = stringResource(R.string.paused),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = stringResource(R.string.menu_hint),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text(
                    text = stringResource(R.string.resume),
                    style = MaterialTheme.typography.labelLarge,
                    color = PiggyAccent
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onQuit) {
                Text(
                    text = stringResource(R.string.quit_round),
                    style = MaterialTheme.typography.labelLarge,
                    color = PiggyTextSecondary
                )
            }
        }
    )
}
