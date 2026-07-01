package com.sveis.karbonekvivalent.data

import com.sveis.karbonekvivalent.db.CeDatabase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Manager for import og eksport av databasen til JSON.
 */
object BackupManager {
    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    fun exportDatabase(database: CeDatabase): String {
        val queries = database.ceDatabaseQueries
        
        // Hent innstillinger
        val dbSettings = queries.getSettings().executeAsOneOrNull()
        val settingsBackup = AppSettingsBackup(
            theme = dbSettings?.theme ?: "FIN",
            language = dbSettings?.language ?: "no"
        )
        
        // Hent alle oppføringer
        val dbEntries = queries.selectAllEntries().executeAsList()
        val entriesBackup = dbEntries.map { 
            CeEntryBackup(
                timestamp = it.timestamp,
                name = it.name,
                carbon = it.carbon,
                manganese = it.manganese,
                chromium = it.chromium,
                molybdenum = it.molybdenum,
                vanadium = it.vanadium,
                nickel = it.nickel,
                copper = it.copper,
                ceResult = it.ceResult,
                iso15608Group = it.iso15608Group,
                silicon = it.silicon,
                sulfur = it.sulfur,
                phosphorus = it.phosphorus,
                niobium = it.niobium,
                titanium = it.titanium
            )
        }
        
        val fullBackup = FullBackup(
            settings = settingsBackup,
            entries = entriesBackup
        )
        
        return json.encodeToString(fullBackup)
    }

    fun importDatabase(database: CeDatabase, jsonString: String) {
        val backup = json.decodeFromString<FullBackup>(jsonString)
        val queries = database.ceDatabaseQueries
        
        // Transaction for å sikre atomisk import
        database.transaction {
            // Oppdater innstillinger
            queries.upsertSettings(
                theme = backup.settings.theme,
                language = backup.settings.language
            )
            
            // Importer alle oppføringer (vi legger til nye, sletter ikke gamle i denne versjonen)
            backup.entries.forEach { 
                queries.insertEntry(
                    timestamp = it.timestamp,
                    name = it.name,
                    ceResult = it.ceResult,
                    iso15608Group = it.iso15608Group,
                    carbon = it.carbon,
                    manganese = it.manganese,
                    chromium = it.chromium,
                    molybdenum = it.molybdenum,
                    vanadium = it.vanadium,
                    nickel = it.nickel,
                    copper = it.copper,
                    silicon = it.silicon,
                    sulfur = it.sulfur,
                    phosphorus = it.phosphorus,
                    niobium = it.niobium,
                    titanium = it.titanium
                )
            }
        }
    }
}
