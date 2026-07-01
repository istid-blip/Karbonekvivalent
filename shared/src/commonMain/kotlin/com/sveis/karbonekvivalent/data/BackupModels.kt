package com.sveis.karbonekvivalent.data

import kotlinx.serialization.Serializable

@Serializable
data class CeEntryBackup(
    val timestamp: Long,
    val name: String? = null,
    val carbon: Double,
    val manganese: Double,
    val chromium: Double,
    val molybdenum: Double,
    val vanadium: Double,
    val nickel: Double,
    val copper: Double,
    val ceResult: Double,
    val iso15608Group: String? = null,
    val silicon: Double = 0.0,
    val sulfur: Double = 0.0,
    val phosphorus: Double = 0.0,
    val niobium: Double = 0.0,
    val titanium: Double = 0.0
)

@Serializable
data class AppSettingsBackup(
    val theme: String,
    val language: String
)

@Serializable
data class FullBackup(
    val version: Int = 1,
    val settings: AppSettingsBackup,
    val entries: List<CeEntryBackup>
)
