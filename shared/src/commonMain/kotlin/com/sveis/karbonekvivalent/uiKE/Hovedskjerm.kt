package com.sveis.karbonekvivalent.uiKE

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.sveis.karbonekvivalent.KEKalkulator
import com.sveis.karbonekvivalent.util.KeepScreenOn
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import com.sveis.karbonekvivalent.data.SteelAlloy
import com.sveis.karbonekvivalent.data.defaultSteelAlloys
import karbonekvivalent.shared.generated.resources.Res
import karbonekvivalent.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
private fun HovedSkjermHeader(
    onApneInnstillinger: () -> Unit,
    onApneHistorikk: () -> Unit,
    dimmet: Boolean = false,
    modifier: Modifier = Modifier,
) {
    AppHeader(
        tittel = stringResource(Res.string.app_title),
        venstreIkon = Icons.Default.Settings,
        venstreIkonBeskrivelse = stringResource(Res.string.settings),
        onVenstreKlikk = onApneInnstillinger,
        hoyreIkon = Icons.Default.History,
        hoyreIkonBeskrivelse = stringResource(Res.string.history),
        onHoyreKlikk = onApneHistorikk,
        dimmet = dimmet,
        modifier = modifier
    )
}

@Composable
fun HovedSkjerm(
    language: String,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onSave: (Double, Double, Double, Double, Double, Double, Double, Double) -> Unit,
    scrollState: ScrollState = rememberScrollState(),
    formulaScrollState: ScrollState = rememberScrollState(),
) {
    // Sørger for at skjermen ikke slår seg av mens vi er på hovedskjermen
    KeepScreenOn()

    // State for alle kjemiske elementer
    var carbon by remember { mutableStateOf(0.15) }
    var manganese by remember { mutableStateOf(1.20) }
    var chromium by remember { mutableStateOf(0.0) }
    var molybdenum by remember { mutableStateOf(0.0) }
    var vanadium by remember { mutableStateOf(0.0) }
    var nickel by remember { mutableStateOf(0.0) }
    var copper by remember { mutableStateOf(0.0) }

    var selectedAlloy by remember { mutableStateOf<SteelAlloy?>(defaultSteelAlloys.first()) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    var aktivtElement by remember { mutableStateOf<String?>(null) }

    var erILagreModus by remember { mutableStateOf(false) }
    var alloyName by remember { mutableStateOf("") }

    val ceResult = KEKalkulator.calculateCE(
        carbon, manganese, chromium, molybdenum, vanadium, nickel, copper,
    )

    Scaffold(
        // FAB fjernet
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                HovedSkjermHeader(
                    onApneInnstillinger = onNavigateToSettings,
                    onApneHistorikk = onNavigateToHistory,
                    dimmet = aktivtElement != null || erILagreModus,
                    modifier = Modifier.zIndex(2f)
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    // Scrim
                    if (dropdownExpanded || erILagreModus) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .zIndex(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { 
                                        dropdownExpanded = false
                                        if (erILagreModus) {
                                            erILagreModus = false
                                            alloyName = ""
                                        }
                                    }
                                )
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp, bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // --- SEKSJON 1: VALG OG RESULTAT ---
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            SelectorAndResultSection(
                                selectorVerdi = selectedAlloy?.name ?: "",
                                selectorLabel = "STÅLLEGERING",
                                dropdownExpanded = dropdownExpanded,
                                erNoeAktivt = aktivtElement != null || erILagreModus,
                                resultatTittel = stringResource(Res.string.result_title),
                                resultatVerdi = ceResult.toString().take(4).replace(".", ","),
                                resultatUndertekst = KEKalkulator.evaluateWeldability(ceResult).toLocalizedText(),
                                onToggleDropdown = { dropdownExpanded = !dropdownExpanded },
                                onDismissDropdown = { dropdownExpanded = false },
                                containerColor = MaterialTheme.colorScheme.background,
                                dropdownInnhold = {
                                    defaultSteelAlloys.forEach { alloy ->
                                        DropdownMenuItem(
                                            text = {
                                                Column(modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        text = alloy.name,
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    if (alloy.name != "Egendefinert") {
                                                        Text(
                                                            text = "C: ${alloy.carbon}%, Mn: ${alloy.manganese}%",
                                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            },
                                            onClick = {
                                                selectedAlloy = alloy
                                                if (alloy.name != "Egendefinert") {
                                                    carbon = alloy.carbon
                                                    manganese = alloy.manganese
                                                    chromium = alloy.chromium
                                                    molybdenum = alloy.molybdenum
                                                    vanadium = alloy.vanadium
                                                    nickel = alloy.nickel
                                                    copper = alloy.copper
                                                }
                                                dropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            )
                        }

                        // --- SEKSJON 2: FORMEL OG LAGRING ---
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer { alpha = if (dropdownExpanded) 0.4f else 1.0f },
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val formulaPanelBg = MaterialTheme.colorScheme.surfaceVariant
                            
                            Surface(
                                modifier = Modifier.fillMaxWidth().height(185.dp),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                color = formulaPanelBg
                            ) {
                                AnimatedContent(
                                    targetState = erILagreModus,
                                    transitionSpec = {
                                        fadeIn(tween(220, delayMillis = 90)) + scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)) togetherWith
                                        fadeOut(tween(90))
                                    }
                                ) { targetIsSaving ->
                                    if (targetIsSaving) {
                                        // Lagrings-visning med tekstfelt
                                        Column(
                                            modifier = Modifier.padding(16.dp),
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = stringResource(Res.string.save_job_header), 
                                                style = MaterialTheme.typography.labelSmall, 
                                                color = MaterialTheme.colorScheme.primary, 
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            OutlinedTextField(
                                                value = alloyName,
                                                onValueChange = { alloyName = it },
                                                modifier = Modifier.fillMaxWidth(),
                                                label = { Text(stringResource(Res.string.job_name_label)) },
                                                singleLine = true,
                                                shape = RoundedCornerShape(12.dp),
                                                colors = TextFieldDefaults.colors(
                                                    focusedContainerColor = Color.Transparent,
                                                    unfocusedContainerColor = Color.Transparent,
                                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                                                )
                                            )
                                            
                                            Spacer(modifier = Modifier.height(16.dp))
                                            
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                KEButton(
                                                    onClick = { 
                                                        erILagreModus = false
                                                        alloyName = ""
                                                    },
                                                    text = stringResource(Res.string.cancel_button_caps),
                                                    isPrimary = false,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                KEButton(
                                                    onClick = {
                                                        onSave(carbon, manganese, chromium, molybdenum, vanadium, nickel, copper, ceResult)
                                                        erILagreModus = false
                                                        alloyName = ""
                                                    },
                                                    text = stringResource(Res.string.save_button_caps),
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                        }
                                    } else {
                                        // Standard formel-visning
                                        CeFormelInputPanel(
                                            carbon = carbon,
                                            manganese = manganese,
                                            chromium = chromium,
                                            molybdenum = molybdenum,
                                            vanadium = vanadium,
                                            nickel = nickel,
                                            copper = copper,
                                            aktivtElement = aktivtElement,
                                            onElementClick = { element ->
                                                aktivtElement = if (aktivtElement == element) null else element
                                            },
                                            inputBakgrunn = formulaPanelBg,
                                            scrollState = formulaScrollState,
                                        )
                                    }
                                }
                            }

                            if (!erILagreModus) {
                                KEButton(
                                    onClick = { erILagreModus = true },
                                    text = stringResource(Res.string.save),
                                    modifier = Modifier.fillMaxWidth(),
                                    useHoldToConfirm = false
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))
                    }

                    // Ny VelgeContainer som et overlegg for rullehjulet
                    VelgeContainer(
                        visArk = aktivtElement != null,
                        fraToppen = false,
                        onLukkBehov = { aktivtElement = null }
                    ) {
                        aktivtElement?.let { element ->
                            when (element) {
                                "C" -> TallVelgerMotor(label = "Carbon", verdi = carbon, onVerdiChange = { carbon = it })
                                "Mn" -> TallVelgerMotor(label = "Manganese", verdi = manganese, onVerdiChange = { manganese = it }, steg = 0.05)
                                "Cr" -> TallVelgerMotor(label = "Chromium", verdi = chromium, onVerdiChange = { chromium = it })
                                "Mo" -> TallVelgerMotor(label = "Molybdenum", verdi = molybdenum, onVerdiChange = { molybdenum = it })
                                "V" -> TallVelgerMotor(label = "Vanadium", verdi = vanadium, onVerdiChange = { vanadium = it })
                                "Ni" -> TallVelgerMotor(label = "Nickel", verdi = nickel, onVerdiChange = { nickel = it })
                                "Cu" -> TallVelgerMotor(label = "Copper", verdi = copper, onVerdiChange = { copper = it })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HovedSkjermRetroPreview() {
    AppTheme(valgtTema = AppThemeType.RETRO) {
        HovedSkjerm(
            language = "no",
            onNavigateToHistory = {},
            onNavigateToSettings = {},
            onSave = { _, _, _, _, _, _, _, _ -> },
            scrollState = rememberScrollState(),
            formulaScrollState = rememberScrollState(),
        )
    }
}

@Preview
@Composable
fun HovedSkjermFinPreview() {
    AppTheme(valgtTema = AppThemeType.FIN) {
        HovedSkjerm(
            language = "no",
            onNavigateToHistory = {},
            onNavigateToSettings = {},
            onSave = { _, _, _, _, _, _, _, _ -> },
            scrollState = rememberScrollState(),
            formulaScrollState = rememberScrollState(),
        )
    }
}
