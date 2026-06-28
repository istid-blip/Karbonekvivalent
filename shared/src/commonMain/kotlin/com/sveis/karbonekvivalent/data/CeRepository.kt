package com.sveis.karbonekvivalent.data

import com.sveis.karbonekvivalent.db.CeDatabase
import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.flow.Flow
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import com.sveis.karbonekvivalent.db.CeEntry

class CeRepository(driver: SqlDriver) {
    private val database = CeDatabase(driver)
    private val queries = database.ceDatabaseQueries

    fun getAllEntries(): Flow<List<CeEntry>> {
        return queries.selectAllEntries().asFlow().mapToList(Dispatchers.IO)
    }

    fun insertEntry(
        carbon: Double,
        manganese: Double,
        chromium: Double,
        molybdenum: Double,
        vanadium: Double,
        nickel: Double,
        copper: Double,
        ceResult: Double,
    ) {
        queries.insertEntry(
            timestamp = 0L,
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
