package com.sveis.karbonekvivalent.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

actual object LocalAppLocale {
    private val LocalAppLocale = staticCompositionLocalOf { 
        (NSLocale.preferredLanguages.firstOrNull() as? String) ?: "no"
    }

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val langKey = "AppleLanguages"
        val new = value ?: "no"
        
        // På iOS overstyrer vi system-språket for appen ved å sette AppleLanguages
        if (value == null) {
            NSUserDefaults.standardUserDefaults.removeObjectForKey(langKey)
        } else {
            NSUserDefaults.standardUserDefaults.setObject(listOf(new), langKey)
        }
        
        return LocalAppLocale.provides(new)
    }
}
