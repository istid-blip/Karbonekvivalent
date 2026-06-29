package com.sveis.karbonekvivalent.util

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

actual object LocalAppLocale {
    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val configuration = LocalConfiguration.current
        val context = LocalContext.current
        
        val newLocale = when (value) {
            "en" -> Locale.ENGLISH
            "no" -> Locale("no")
            else -> Locale.getDefault()
        }
        
        Locale.setDefault(newLocale)
        val newConfig = Configuration(configuration)
        newConfig.setLocale(newLocale)
        
        // Oppdaterer ressursene slik at stringResource plukker opp riktig XML
        context.resources.updateConfiguration(newConfig, context.resources.displayMetrics)
        
        return LocalConfiguration.provides(newConfig)
    }
}
