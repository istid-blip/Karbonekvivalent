package com.sveis.karbonekvivalent.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.sveis.karbonekvivalent.db.CeDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(CeDatabase.Schema, "ce_database.db")
    }
}
