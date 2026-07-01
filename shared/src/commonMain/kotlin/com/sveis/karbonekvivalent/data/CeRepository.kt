package com.sveis.karbonekvivalent.data

import com.sveis.karbonekvivalent.db.CeDatabase
import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.flow.Flow
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import com.sveis.karbonekvivalent.db.CeEntry
import com.sveis.karbonekvivalent.db.AppSettings
import com.sveis.karbonekvivalent.getCurrentTimeMillis
import kotlinx.coroutines.withContext

class CeRepository(driver: SqlDriver) {
    val database = CeDatabase(driver)
    private val queries = database.ceDatabaseQueries

    fun getAllEntries(): Flow<List<CeEntry>> {
        return queries.selectAllEntries().asFlow().mapToList(Dispatchers.IO)
    }

    fun getSettings(): Flow<AppSettings?> {
        return queries.getSettings().asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    suspend fun initializeSettings() = withContext(Dispatchers.IO) {
        if (queries.getSettings().executeAsOneOrNull() == null) {
            queries.upsertSettings(theme = "FIN", language = "no")
        }
    }

    suspend fun updateTheme(theme: String) = withContext(Dispatchers.IO) {
        queries.updateTheme(theme)
    }

    suspend fun updateLanguage(language: String) = withContext(Dispatchers.IO) {
        queries.updateLanguage(language)
    }

    suspend fun insertEntry(
        name: String?,
        carbon: Double,
        manganese: Double,
        chromium: Double,
        molybdenum: Double,
        vanadium: Double,
        nickel: Double,
        copper: Double,
        ceResult: Double,
    ) = withContext(Dispatchers.IO) {
        queries.insertEntry(
            timestamp = getCurrentTimeMillis(),
            name = name,
            carbon = carbon,
            manganese = manganese,
            chromium = chromium,
            molybdenum = molybdenum,
            vanadium = vanadium,
            nickel = nickel,
            copper = copper,
            ceResult = ceResult
        )
    }
}
