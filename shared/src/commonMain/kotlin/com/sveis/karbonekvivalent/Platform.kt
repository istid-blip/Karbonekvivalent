package com.sveis.karbonekvivalent

/**
 * Dette grensesnittet definerer plattformspesifikk informasjon i et Kotlin Multiplatform (KMP) prosjekt.
 * Det brukes sammen med 'expect/actual' mekanismen for å hente ut informasjon fra den underliggende 
 * plattformen (Android, iOS, etc.) i felleskode (commonMain).
 *
 * Implementasjoner finnes i:
 * - Platform.android.kt (for Android-spesifikk logikk)
 * - Platform.ios.kt (for iOS-spesifikk logikk)
 */
interface Platform {
    val name: String
}

/**
 * Returnerer en instans av den nåværende plattformens implementasjon.
 */
expect fun getPlatform(): Platform
