package com.sveis.karbonekvivalent.uiKE

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sveis.karbonekvivalent.db.CeDatabase
import com.sveis.karbonekvivalent.data.BackupManager

@Composable
fun Innstillinger(
    database: CeDatabase?,
    valgtTema: AppThemeType,
    onTemaValgt: (AppThemeType) -> Unit,
    valgtEnhet: LengdeEnhet,
    onEnhetValgt: (LengdeEnhet) -> Unit,
    valgtSprak: String,
    onSprakValgt: (String) -> Unit,
    onLukk: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visDisclaimerArk by remember { mutableStateOf(false) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val erNoeApen = visDisclaimerArk
    val useEdgeToEdge = LocalUseEdgeToEdge.current
    val isRetro = valgtTema == AppThemeType.RETRO

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .then(if (useEdgeToEdge) Modifier.statusBarsPadding() else Modifier)
    ) {
        // --- Header ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 16.dp, end = 16.dp)
                .height(48.dp)
        ) {
            AutoResizedText(
                text = "INNSTILLINGER",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Left,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            IconButton(
                onClick = onLukk,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Tilbake",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 70.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            // --- Database Seksjon ---
            SettingsSectionTitle(title = "DATABASE", isRetro = isRetro)

            InnstillingValgKort(
                tittel = "Eksporter historikk",
                hovedtekst = "Lagre backup",
                undertekst = "Eksporter alle lagrede beregninger til JSON-fil",
                infoTekst = "Dette lar deg ta vare på historikken din hvis du bytter telefon.",
                onClick = {
                    // Placeholder for eksport
                }
            )

            InnstillingValgKort(
                tittel = "Importer historikk",
                hovedtekst = "Gjenopprett backup",
                undertekst = "Last inn beregninger fra en JSON-fil",
                infoTekst = "Advarsel: Dette kan overskrive eksisterende data.",
                onClick = {
                    // Placeholder for import
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // --- Språk-seksjon ---
            SettingsSectionTitle(title = "SPRÅK", isRetro = isRetro)

            AppSwitcher(
                selected = valgtSprak,
                options = listOf("no", "en"),
                onSelected = onSprakValgt,
                labelProvider = { it.uppercase() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // --- Tema-seksjon ---
            SettingsSectionTitle(title = "UTSEENDE", isRetro = isRetro)

            AppSwitcher(
                selected = valgtTema,
                options = AppThemeType.entries,
                onSelected = onTemaValgt,
                labelProvider = { it.name }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // --- Enhet-seksjon ---
            SettingsSectionTitle(title = "ENHETER", isRetro = isRetro)

            AppSwitcher(
                selected = valgtEnhet,
                options = LengdeEnhet.entries,
                onSelected = onEnhetValgt,
                labelProvider = { it.name }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            Spacer(modifier = Modifier.height(24.dp))

            // --- Disclaimer ---
            Text(
                text = "Vilkår for bruk og ansvarsfraskrivelse",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isRetro) primaryColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { visDisclaimerArk = true }
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            // --- Copyright ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "KARBONEKVIVALENT",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        letterSpacing = 1.5.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "v1.0.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
        }

        // --- Scrim ---
        if (erNoeApen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { 
                        visDisclaimerArk = false
                    }
            )
        }

        // --- Disclaimer Ark ---
        VelgeContainer(
            visArk = visDisclaimerArk,
            fraToppen = true
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(
                    text = "DISCLAIMER",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
                Text(
                    text = "Denne applikasjonen er kun ment som et veiledende verktøy for beregning av karbonekvivalenter. Beregningene bør verifiseres mot gjeldende standarder og profesjonell vurdering.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String, isRetro: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 16.dp).height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .width(if (isRetro) 4.dp else 2.dp)
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(if (isRetro) 0.dp else 2.dp)
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isRetro) title.uppercase() else title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = if (isRetro) FontWeight.Bold else FontWeight.ExtraBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
