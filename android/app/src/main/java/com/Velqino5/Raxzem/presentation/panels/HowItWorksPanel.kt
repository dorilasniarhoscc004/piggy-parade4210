package com.Velqino5.Raxzem.presentation.panels

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Velqino5.Raxzem.R
import com.Velqino5.Raxzem.core.theme.PiggyAccent
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextSecondary
import com.Velqino5.Raxzem.core.ui.GlassPanelSurface
import com.Velqino5.Raxzem.core.ui.PanelScaffold
import com.Velqino5.Raxzem.core.ui.PrimaryButton
import com.Velqino5.Raxzem.core.ui.SectionHeader

@Composable
fun HowItWorksPanel(onClose: () -> Unit) {
    PanelScaffold(
        title = stringResource(R.string.panel_how_it_works),
        onClose = onClose
    ) {
        SectionHeader(title = stringResource(R.string.menu_tagline))

        StepCard(
            art = R.drawable.sprite_4,
            title = stringResource(R.string.how_step_one),
            body = stringResource(R.string.how_step_one_body)
        )
        StepCard(
            art = R.drawable.sprite_1,
            title = stringResource(R.string.how_step_two),
            body = stringResource(R.string.how_step_two_body)
        )
        StepCard(
            art = R.drawable.sprite_5,
            title = stringResource(R.string.how_step_three),
            body = stringResource(R.string.how_step_three_body)
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrimaryButton(
            text = stringResource(R.string.got_it),
            onClick = onClose
        )
    }
}

@Composable
private fun StepCard(art: Int, title: String, body: String) {
    GlassPanelSurface(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(art),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.padding(end = 4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = PiggyAccent
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = PiggyTextSecondary
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(2.dp))
}
