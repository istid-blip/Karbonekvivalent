package com.sveis.karbonekvivalent

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.sveis.karbonekvivalent.uiKE.Hovedskjerm
import com.sveis.karbonekvivalent.uiKE.HistorikkSkjerm
import com.sveis.karbonekvivalent.uiKE.Innstillinger
import com.sveis.karbonekvivalent.uiKE.AppTheme
import com.sveis.karbonekvivalent.uiKE.AppThemeType
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
    val scope = rememberCoroutineScope()
    val repository = remember { CeRepository(driverFactory.createDriver()) }

    // Initialiserer innstillinger ved oppstart
    LaunchedEffect(Unit) {
        repository.initializeSettings()
    }

    // Observerer innstillinger fra databasen
    val settings by repository.getSettings().collectAsState(initial = null)
    
    // Fallback verdier mens vi venter på databasen
    val darkTheme = settings?.theme ?: "FIN"
    val language = settings?.language ?: "no"
    
    val currentTheme = if (darkTheme == "FIN") AppThemeType.FIN else AppThemeType.RETRO
    
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    // Henter alle lagrede bidrag som en strøm (Flow) og konverterer til Compose State
    val historyEntries by repository.getAllEntries().collectAsState(initial = emptyList())

    AppTheme(valgtTema = currentTheme) {
        when (currentScreen) {
            Screen.Home -> {
                Hovedskjerm(
                    darkTheme = darkTheme == "FIN",
                    onThemeChange = { 
                        scope.launch { 
                            repository.updateTheme(if (darkTheme == "FIN") "RETRO" else "FIN") 
                        } 
                    },
                    language = language,
                    onLanguageChange = { newLang -> 
                        scope.launch { repository.updateLanguage(newLang) } 
                    },
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
                    database = repository.database,
                    valgtTema = currentTheme,
                    onTemaValgt = { newTheme -> 
                        scope.launch { repository.updateTheme(newTheme.name) } 
                    },
                    valgtSprak = language,
                    onSprakValgt = { newLang -> 
                        scope.launch { repository.updateLanguage(newLang) } 
                    },
                    onLukk = { currentScreen = Screen.Home }
                )
            }
        }
    }
}
