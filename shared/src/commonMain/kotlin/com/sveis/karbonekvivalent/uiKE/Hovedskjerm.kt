package com.sveis.karbonekvivalent.uiKE

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

    val ceResult = KEKalkulator.calculateCE(
        carbon, manganese, chromium, molybdenum, vanadium, nickel, copper,
    )

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onSave(carbon, manganese, chromium, molybdenum, vanadium, nickel, copper, ceResult)
                },
                icon = { Text("💾") },
                text = { Text(stringResource(Res.string.save)) }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header flyttet inn hit for å ha bedre kontroll på layering (zIndex)
                HovedSkjermHeader(
                    onApneInnstillinger = onNavigateToSettings,
                    onApneHistorikk = onNavigateToHistory,
                    dimmet = aktivtElement != null,
                    modifier = Modifier.zIndex(2f)
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    // Scrim for å fange opp klikk utenfor dropdown når den er åpen
                    // Ligger under headeren takket være zIndex og rekkefølge
                    if (dropdownExpanded) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .zIndex(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { dropdownExpanded = false }
                                )
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Alloy selection and modern result
                        SelectorAndResultSection(
                            selectorVerdi = selectedAlloy?.name ?: "",
                            selectorLabel = "STÅLLEGERING",
                            dropdownExpanded = dropdownExpanded,
                            erNoeAktivt = aktivtElement != null,
                            resultatTittel = stringResource(Res.string.result_title),
                            resultatVerdi = ceResult.toString().take(4).replace(".", ","),
                            resultatUndertekst = KEKalkulator.evaluateWeldability(ceResult).toLocalizedText(),
                            onToggleDropdown = { dropdownExpanded = !dropdownExpanded },
                            onDismissDropdown = { dropdownExpanded = false },
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

                        // Input-seksjon (Kombinert formel og input)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .graphicsLayer { alpha = if (dropdownExpanded) 0.4f else 1.0f },
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.chemical_composition),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            Surface(
                                modifier = Modifier.fillMaxWidth().height(185.dp),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                            ) {
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
                                    scrollState = formulaScrollState,
                                )
                            }
                        }
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
