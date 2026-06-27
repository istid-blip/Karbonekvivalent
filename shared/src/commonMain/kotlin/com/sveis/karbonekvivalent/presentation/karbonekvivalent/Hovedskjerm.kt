package com.sveis.karbonekvivalent.presentation.karbonekvivalent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sveis.karbonekvivalent.domain.CarbonEquivalentCalculator
import com.sveis.karbonekvivalent.presentation.ui.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Hovedskjerm(
    darkTheme: Boolean,
    onThemeChange: () -> Unit,
    language: String,
    onLanguageChange: (String) -> Unit,
    onNavigateToHistory: () -> Unit,
    onSave: (Double, Double, Double, Double, Double, Double, Double, Double) -> Unit
) {
    // State for alle kjemiske elementer
    var carbon by remember { mutableStateOf(0.15) }
    var manganese by remember { mutableStateOf(1.20) }
    var chromium by remember { mutableStateOf(0.0) }
    var molybdenum by remember { mutableStateOf(0.0) }
    var vanadium by remember { mutableStateOf(0.0) }
    var nickel by remember { mutableStateOf(0.0) }
    var copper by remember { mutableStateOf(0.0) }

    val ceResult = CarbonEquivalentCalculator.calculateCE(
        carbon, manganese, chromium, molybdenum, vanadium, nickel, copper
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CEQ Kalkulator") },
                actions = {
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(Icons.Default.History, contentDescription = "Historikk")
                    }
                    IconButton(onClick = onThemeChange) {
                        Text(if (darkTheme) "🌙" else "☀️")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onSave(carbon, manganese, chromium, molybdenum, vanadium, nickel, copper, ceResult)
                },
                icon = { Text("💾") },
                text = { Text(if(language == "no") "Lagre" else "Save") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Resultat-panel
            StandardKort(tittel = if(language == "no") "Resultat CE (IIW)" else "Result CE (IIW)") {
                Text(
                    text = "CE: " + ceResult.toString().take(5),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Sveisbarhet: ${CarbonEquivalentCalculator.evaluateWeldability(ceResult)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Input-seksjon
            VelgeContainer(tittel = if(language == "no") "Kjemisk sammensetning (%)" else "Chemical composition (%)") {
                TallVelgerMotor(label = "Karbon (C)", verdi = carbon, onVerdiChange = { carbon = it }, steg = 0.01)
                TallVelgerMotor(label = "Mangan (Mn)", verdi = manganese, onVerdiChange = { manganese = it }, steg = 0.05)
                TallVelgerMotor(label = "Krom (Cr)", verdi = chromium, onVerdiChange = { chromium = it }, steg = 0.01)
                TallVelgerMotor(label = "Molybden (Mo)", verdi = molybdenum, onVerdiChange = { molybdenum = it }, steg = 0.01)
                TallVelgerMotor(label = "Vanadium (V)", verdi = vanadium, onVerdiChange = { vanadium = it }, steg = 0.01)
                TallVelgerMotor(label = "Nikkel (Ni)", verdi = nickel, onVerdiChange = { nickel = it }, steg = 0.01)
                TallVelgerMotor(label = "Kobber (Cu)", verdi = copper, onVerdiChange = { copper = it }, steg = 0.01)
            }
        }
    }
}
