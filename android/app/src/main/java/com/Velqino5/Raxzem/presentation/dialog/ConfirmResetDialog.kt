package com.Velqino5.Raxzem.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Velqino5.Raxzem.R
import com.Velqino5.Raxzem.core.theme.PiggyDanger
import com.Velqino5.Raxzem.core.theme.PiggySurface
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextSecondary

@Composable
fun ConfirmResetDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = PiggySurface,
        titleContentColor = PiggyTextPrimary,
        textContentColor = PiggyTextSecondary,
        title = {
            Text(
                text = stringResource(R.string.confirm_reset_title),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = stringResource(R.string.confirm_reset_body),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.reset),
                    style = MaterialTheme.typography.labelLarge,
                    color = PiggyDanger
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = PiggyTextSecondary
                )
            }
        }
    )
}
