package com.sveis.karbonekvivalent

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.sveis.karbonekvivalent.uiKE.Hovedskjerm
import com.sveis.karbonekvivalent.uiKE.HistorikkSkjerm
import com.sveis.karbonekvivalent.uiKE.Innstillinger
import com.sveis.karbonekvivalent.uiKE.AppTheme
import com.sveis.karbonekvivalent.uiKE.AppThemeType
import com.sveis.karbonekvivalent.uiKE.LengdeEnhet
import com.sveis.karbonekvivalent.data.CeRepository
import com.sveis.karbonekvivalent.data.DatabaseDriverFactory
import com.sveis.karbonekvivalent.db.CeEntry
import kotlinx.coroutines.launch

/**
 * Definerer de tilgjengelige skjermene i applikasjonen for enkel navigasjon.
 */
enum class Screen {
    Home, History, Settings
}

/**
 * Hoved-Composable for Karbonekvivalent-appen i felleskoden (commonMain).
 *
 * Denne funksjonen fungerer som rot-komponenten for brukergrensesnittet og håndterer:
 * - Overordnet tilstand (mørkt tema, språk, navigasjon).
 * - Initialisering av database og repository.
 * - Navigasjon mellom hovedskjermen (Hovedskjerm) og historikkvisningen (HistorikkSkjerm).
 * - Koordinering av lagring av beregninger til databasen.
 *
 * @param driverFactory Fabrikk for å opprette database-drivere på tvers av plattformer (Android/iOS).
 */
@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    var darkTheme by rememberSaveable { mutableStateOf(true) }
    var language by rememberSaveable { mutableStateOf("no") }
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    val scope = rememberCoroutineScope()
    // Initialiserer repository for datatilgang
    val repository = remember { CeRepository(driverFactory.createDriver()) }
    // Henter alle lagrede bidrag som en strøm (Flow) og konverterer til Compose State
    val historyEntries by repository.getAllEntries().collectAsState(initial = emptyList())

    AppTheme(valgtTema = if (darkTheme) AppThemeType.FIN else AppThemeType.RETRO) {
        when (currentScreen) {
            Screen.Home -> {
                Hovedskjerm(
                    darkTheme = darkTheme,
                    onThemeChange = { darkTheme = !darkTheme },
                    language = language,
                    onLanguageChange = { language = it },
                    onNavigateToHistory = { currentScreen = Screen.History },
                    onNavigateToSettings = { currentScreen = Screen.Settings },
                    onSave = { c, mn, cr, mo, v, ni, cu, res ->
                        scope.launch {
                            repository.insertEntry(c, mn, cr, mo, v, ni, cu, res)
                        }
                    }
                )
            }
            Screen.History -> {
                HistorikkSkjerm(
                    entries = historyEntries,
                    onBack = { currentScreen = Screen.Home }
                )
            }
            Screen.Settings -> {
                Innstillinger(
                    database = null, // TODO: Send database
                    valgtTema = if (darkTheme) AppThemeType.FIN else AppThemeType.RETRO,
                    onTemaValgt = { darkTheme = (it == AppThemeType.FIN) },
                    valgtEnhet = LengdeEnhet.MM,
                    onEnhetValgt = { },
                    valgtSprak = language,
                    onSprakValgt = { language = it },
                    onLukk = { currentScreen = Screen.Home }
                )
            }
        }
    }
}
