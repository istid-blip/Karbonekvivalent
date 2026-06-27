package com.sveis.karbonekvivalent.domain

/**
 * Weldability levels based on the Carbon Equivalent (CE) value.
 */
enum class Weldability {
    EXCELLENT,
    GOOD,
    POOR
}

/**
 * Calculator for Carbon Equivalent (CE) using the standard IIW formula.
 *
 * The IIW (International Institute of Welding) formula is:
 * CE = C + (Mn/6) + ((Cr + Mo + V)/5) + ((Ni + Cu)/15)
 */
object CarbonEquivalentCalculator {

    /**
     * Calculates the Carbon Equivalent (CE) value.
     *
     * @param carbon C percentage
     * @param manganese Mn percentage
     * @param chromium Cr percentage
     * @param molybdenum Mo percentage
     * @param vanadium V percentage
     * @param nickel Ni percentage
     * @param copper Cu percentage
     * @return The calculated CE value.
     */
    fun calculateCE(
        carbon: Double,
        manganese: Double,
        chromium: Double,
        molybdenum: Double,
        vanadium: Double,
        nickel: Double,
        copper: Double
    ): Double {
        return carbon + (manganese / 6.0) + ((chromium + molybdenum + vanadium) / 5.0) + ((nickel + copper) / 15.0)
    }

    /**
     * Evaluates the weldability based on the CE value.
     *
     * - Excellent: CE <= 0.35
     * - Good: 0.36 <= CE <= 0.55
     * - Poor: CE > 0.55
     *
     * @param ceValue The calculated CE value.
     * @return The corresponding [Weldability] level.
     */
    fun evaluateWeldability(ceValue: Double): Weldability {
        return when {
            ceValue <= 0.35 -> Weldability.EXCELLENT
            ceValue <= 0.55 -> Weldability.GOOD
            else -> Weldability.POOR
        }
    }
}
