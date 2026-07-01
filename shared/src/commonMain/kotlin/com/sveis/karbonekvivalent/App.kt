package com.sveis.karbonekvivalent

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import com.sveis.karbonekvivalent.uiKE.HovedSkjerm
import com.sveis.karbonekvivalent.uiKE.HistorikkSkjerm
import com.sveis.karbonekvivalent.uiKE.InnstillingerSkjerm
import com.sveis.karbonekvivalent.uiKE.AppTheme
import com.sveis.karbonekvivalent.uiKE.AppThemeType
import com.sveis.karbonekvivalent.data.CeRepository
import com.sveis.karbonekvivalent.data.DatabaseDriverFactory
import com.sveis.karbonekvivalent.util.AppEnvironment
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

    // Bevarer rulleposisjon for de ulike skjermene
    val homeScrollState = rememberScrollState()
    val formulaScrollState = rememberScrollState()
    val historyListState = rememberLazyListState()
    val settingsScrollState = rememberScrollState()

    AppTheme(valgtTema = currentTheme) {
        AppEnvironment(language = language) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    val targetIndex = when (targetState) {
                        Screen.Settings -> 0
                        Screen.Home -> 1
                        Screen.History -> 2
                    }
                    val initialIndex = when (initialState) {
                        Screen.Settings -> 0
                        Screen.Home -> 1
                        Screen.History -> 2
                    }

                    if (targetIndex > initialIndex) {
                        // Navigerer "fremover" (mot høyre) -> Innhold kommer fra høyre, gammelt går ut til venstre
                        slideInHorizontally(animationSpec = tween(300)) { it } togetherWith
                                slideOutHorizontally(animationSpec = tween(300)) { -it }
                    } else {
                        // Navigerer "bakover" (mot venstre) -> Innhold kommer fra venstre, gammelt går ut til høyre
                        slideInHorizontally(animationSpec = tween(300)) { -it } togetherWith
                                slideOutHorizontally(animationSpec = tween(300)) { it }
                    }
                },
                label = "skjermOvergang",
            ) { screen ->
                when (screen) {
                    Screen.Home -> {
                        HovedSkjerm(
                            language = language,
                            onNavigateToHistory = { currentScreen = Screen.History },
                            onNavigateToSettings = { currentScreen = Screen.Settings },
                            onSave = { name, c, mn, cr, mo, v, ni, cu, res, iso, si, s, p, nb, ti ->
                                scope.launch {
                                    repository.insertEntry(name, c, mn, cr, mo, v, ni, cu, res, iso, si, s, p, nb, ti)
                                }
                            },
                            scrollState = homeScrollState,
                            formulaScrollState = formulaScrollState,
                        )
                    }
                    Screen.History -> {
                        HistorikkSkjerm(
                            entries = historyEntries,
                            language = language,
                            onBack = { currentScreen = Screen.Home },
                            listState = historyListState,
                        )
                    }
                    Screen.Settings -> {
                        InnstillingerSkjerm(
                            database = repository.database,
                            valgtTema = currentTheme,
                            onTemaValgt = { newTheme ->
                                scope.launch { repository.updateTheme(newTheme.name) }
                            },
                            valgtSprak = language,
                            onSprakValgt = { newLang ->
                                scope.launch { repository.updateLanguage(newLang) }
                            },
                            onLukk = { currentScreen = Screen.Home },
                            scrollState = settingsScrollState,
                        )
                    }
                }
            }
        }
    }
}
