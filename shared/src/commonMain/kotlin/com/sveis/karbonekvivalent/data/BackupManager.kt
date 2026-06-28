package com.sveis.karbonekvivalent.data

import com.sveis.karbonekvivalent.db.CeDatabase

/**
 * Enkel manager for import og eksport av databasen.
 * Foreløpig en placeholder for fremtidig implementasjon.
 */
object BackupManager {
    fun exportDatabase(database: CeDatabase?): String {
        // TODO: Implementer faktisk JSON-eksport av CeEntry tabellen
        return "{ \"version\": 1, \"entries\": [] }"
    }

    fun importDatabase(database: CeDatabase?, json: String) {
        // TODO: Implementer faktisk import-logikk
    }
}
