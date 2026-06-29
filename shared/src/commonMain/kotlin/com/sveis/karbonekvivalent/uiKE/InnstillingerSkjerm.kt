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
import com.sveis.karbonekvivalent.util.shareBackupFile
import com.sveis.karbonekvivalent.util.saveBackupFile
import karbonekvivalent.shared.generated.resources.Res
import karbonekvivalent.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun InnstillingerSkjerm(
    database: CeDatabase?,
    valgtTema: AppThemeType,
    onTemaValgt: (AppThemeType) -> Unit,
    valgtSprak: String,
    onSprakValgt: (String) -> Unit,
    onLukk: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var visDisclaimerArk by remember { mutableStateOf(value = false) }
    var erEksportUtvidet by remember { mutableStateOf(value = false) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val erNoeApen = visDisclaimerArk
    val useEdgeToEdge = LocalUseEdgeToEdge.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .then(if (useEdgeToEdge) Modifier.statusBarsPadding() else Modifier)
            .swipeToDismiss(
                onDismiss = onLukk,
                swipeDirection = SwipeDirection.LEFT
            )
    ) {
        // --- Header ---
        val isRetro = valgtTema == AppThemeType.RETRO
        AppHeader(
            tittel = stringResource(Res.string.settings),
            hoyreIkon = Icons.AutoMirrored.Filled.ArrowForward,
            hoyreIkonBeskrivelse = stringResource(Res.string.back),
            onHoyreKlikk = onLukk,
            tittelAlignment = Alignment.CenterStart,
            tittelStil = MaterialTheme.typography.headlineSmall
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 60.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            // --- Database Seksjon ---
            SettingsSectionTitle(title = stringResource(Res.string.database_section), isRetro = isRetro)

            InnstillingValgKort(
                tittel = stringResource(Res.string.export_history_title),
                hovedtekst = stringResource(Res.string.export_history_main),
                undertekst = stringResource(Res.string.export_history_sub),
                infoTekst = stringResource(Res.string.export_history_info),
                erUtvidet = erEksportUtvidet,
                onClick = { erEksportUtvidet = !erEksportUtvidet },
                ekstraInnhold = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                database?.let { db ->
                                    val json = BackupManager.exportDatabase(db)
                                    saveBackupFile(json, "karbonekvivalent_backup.json")
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = if (isRetro) RoundedCornerShape(0.dp) else ButtonDefaults.shape
                        ) {
                            Text(stringResource(Res.string.save_to_device))
                        }
                        
                        OutlinedButton(
                            onClick = {
                                database?.let { db ->
                                    val json = BackupManager.exportDatabase(db)
                                    shareBackupFile(json, "karbonekvivalent_backup.json")
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = if (isRetro) RoundedCornerShape(0.dp) else ButtonDefaults.outlinedShape
                        ) {
                            Text(stringResource(Res.string.share_with_app))
                        }
                    }
                }
            )

            InnstillingValgKort(
                tittel = stringResource(Res.string.import_history_title),
                hovedtekst = stringResource(Res.string.import_history_main),
                undertekst = stringResource(Res.string.import_history_sub),
                infoTekst = stringResource(Res.string.import_history_info),
                onClick = {
                    // Placeholder for import-velger
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // --- Språk-seksjon ---
            SettingsSectionTitle(title = stringResource(Res.string.language_section), isRetro = isRetro)

            AppSwitcher(
                selected = valgtSprak,
                options = listOf("no", "en"),
                onSelected = onSprakValgt,
                labelProvider = { it.uppercase() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // --- Tema-seksjon ---
            SettingsSectionTitle(title = stringResource(Res.string.appearance_section), isRetro = isRetro)

            AppSwitcher(
                selected = valgtTema,
                options = AppThemeType.entries,
                onSelected = onTemaValgt,
                labelProvider = { it.name }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            Spacer(modifier = Modifier.height(24.dp))

            // --- Disclaimer ---
            Text(
                text = stringResource(Res.string.disclaimer_link),
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
                    text = stringResource(Res.string.app_title).uppercase(),
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
                    text = stringResource(Res.string.disclaimer_title),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
                Text(
                    text = stringResource(Res.string.disclaimer_text),
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
