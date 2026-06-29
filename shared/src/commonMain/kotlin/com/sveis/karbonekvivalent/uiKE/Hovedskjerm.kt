package com.sveis.karbonekvivalent.uiKE

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sveis.karbonekvivalent.KEKalkulator
import com.sveis.karbonekvivalent.util.KeepScreenOn
import androidx.compose.ui.tooling.preview.Preview
import karbonekvivalent.shared.generated.resources.Res
import karbonekvivalent.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun HovedSkjerm(
    language: String,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onSave: (Double, Double, Double, Double, Double, Double, Double, Double) -> Unit,
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

    var aktivtElement by remember { mutableStateOf<String?>(null) }

    val ceResult = KEKalkulator.calculateCE(
        carbon, manganese, chromium, molybdenum, vanadium, nickel, copper,
    )

    Scaffold(
        topBar = {
            HovedSkjermHeader(
                onApneInnstillinger = onNavigateToSettings,
                onApneHistorikk = onNavigateToHistory,
                language = language,
                dimmet = aktivtElement != null
            )
        },
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
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Resultat-panel
                StandardKort(tittel = stringResource(Res.string.result_title)) {
                    Text(
                        text = "CE: " + ceResult.toString().take(5),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${stringResource(Res.string.weldability)}: ${KEKalkulator.evaluateWeldability(ceResult).toLocalizedText()}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Input-seksjon (Kombinert formel og input)
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
                            }
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

@Preview
@Composable
fun HovedSkjermRetroPreview() {
    AppTheme(valgtTema = AppThemeType.RETRO) {
        HovedSkjerm(
            language = "no",
            onNavigateToHistory = {},
            onNavigateToSettings = {},
            onSave = { _, _, _, _, _, _, _, _ -> },
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
        )
    }
}
