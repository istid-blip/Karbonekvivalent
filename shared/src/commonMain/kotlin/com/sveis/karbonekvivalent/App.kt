package com.sveis.karbonekvivalent

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.sveis.karbonekvivalent.presentation.karbonekvivalent.Hovedskjerm
import com.sveis.karbonekvivalent.presentation.karbonekvivalent.HistorikkSkjerm
import com.sveis.karbonekvivalent.presentation.ui.AppTheme
import com.sveis.karbonekvivalent.data.CeRepository
import com.sveis.karbonekvivalent.data.DatabaseDriverFactory
import com.sveis.karbonekvivalent.db.CeEntry
import kotlinx.coroutines.launch

enum class Screen {
    Home, History
}

@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    var darkTheme by rememberSaveable { mutableStateOf(true) }
    var language by rememberSaveable { mutableStateOf("no") }
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    val scope = rememberCoroutineScope()
    val repository = remember { CeRepository(driverFactory.createDriver()) }
    val historyEntries by repository.getAllEntries().collectAsState(initial = emptyList())

    AppTheme(darkTheme = darkTheme) {
        when (currentScreen) {
            Screen.Home -> {
                Hovedskjerm(
                    darkTheme = darkTheme,
                    onThemeChange = { darkTheme = !darkTheme },
                    language = language,
                    onLanguageChange = { language = it },
                    onNavigateToHistory = { currentScreen = Screen.History },
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
        }
    }
}
