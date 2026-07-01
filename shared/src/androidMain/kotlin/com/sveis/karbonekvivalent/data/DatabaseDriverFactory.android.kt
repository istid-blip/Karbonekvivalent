package com.sveis.karbonekvivalent.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.sveis.karbonekvivalent.db.CeDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(CeDatabase.Schema, context, "ce_database_v2.db")
    }
}
