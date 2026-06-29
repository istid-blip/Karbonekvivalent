package com.sveis.karbonekvivalent.util

import androidx.compose.runtime.*

/**
 * Hjelpefunksjon for å håndtere språkmiljø i appen.
 */
@Composable
fun AppEnvironment(language: String, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAppLocale provides language,
    ) {
        key(language) {
            content()
        }
    }
}

/**
 * Plattform-spesifikk overstyring av locale.
 */
expect object LocalAppLocale {
    @Composable
    infix fun provides(value: String?): ProvidedValue<*>
}
