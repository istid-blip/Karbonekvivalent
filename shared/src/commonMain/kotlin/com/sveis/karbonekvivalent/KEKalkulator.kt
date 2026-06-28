package com.sveis.karbonekvivalent

/**
 * Definerer nivåer for sveisbarhet basert på verdien for karbonekvivalent (CE).
 */
enum class Weldability {
    EXCELLENT,
    GOOD,
    POOR
}

/**
 * Kalkulator for beregning av karbonekvivalent (CE) ved bruk av standard IIW-formel.
 *
 * IIW (International Institute of Welding) formelen er:
 * CE = C + (Mn/6) + ((Cr + Mo + V)/5) + ((Ni + Cu)/15)
 *
 * Denne verdien brukes for å vurdere risikoen for herdesprekker ved sveising av stål.
 */
object KEKalkulator {

    /**
     * Beregner verdien for karbonekvivalent (CE).
     *
     * @param carbon C prosentandel (Karbon)
     * @param manganese Mn prosentandel (Mangan)
     * @param chromium Cr prosentandel (Krom)
     * @param molybdenum Mo prosentandel (Molybden)
     * @param vanadium V prosentandel (Vanadium)
     * @param nickel Ni prosentandel (Nikkel)
     * @param copper Cu prosentandel (Kobber)
     * @return Den beregnede CE-verdien.
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
     * Vurderer sveisbarheten basert på den beregnede CE-verdien.
     *
     * Skala for vurdering:
     * - Utmerket (Excellent): CE <= 0.35
     * - God (Good): 0.36 <= CE <= 0.55
     * - Dårlig (Poor): CE > 0.55
     *
     * @param ceValue Den beregnede CE-verdien.
     * @return Tilsvarende [Weldability] nivå.
     */
    fun evaluateWeldability(ceValue: Double): Weldability {
        return when {
            ceValue <= 0.35 -> Weldability.EXCELLENT
            ceValue <= 0.55 -> Weldability.GOOD
            else -> Weldability.POOR
        }
    }
}
