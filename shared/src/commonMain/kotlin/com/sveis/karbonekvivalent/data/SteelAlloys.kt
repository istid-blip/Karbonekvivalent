package com.sveis.karbonekvivalent.data

import kotlinx.serialization.Serializable

@Serializable
data class SteelAlloy(
    val name: String,
    val carbon: Double = 0.0,
    val manganese: Double = 0.0,
    val chromium: Double = 0.0,
    val molybdenum: Double = 0.0,
    val vanadium: Double = 0.0,
    val nickel: Double = 0.0,
    val copper: Double = 0.0,
    val silicon: Double = 0.0,
    val sulfur: Double = 0.0,
    val phosphorus: Double = 0.0,
    val niobium: Double = 0.0,
    val titanium: Double = 0.0
)

val defaultSteelAlloys = listOf(
    SteelAlloy("Standard Stål", 0.15, 1.20, 0.0, 0.0, 0.0, 0.0, 0.0),
    SteelAlloy("S355", 0.20, 1.60, 0.0, 0.0, 0.0, 0.0, 0.0),
    SteelAlloy("Egendefinert", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
)
