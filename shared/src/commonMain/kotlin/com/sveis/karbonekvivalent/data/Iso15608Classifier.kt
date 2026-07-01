package com.sveis.karbonekvivalent.data

/**
 * Enkel klassifisering i henhold til ISO 15608:2017 for stål.
 * Denne er oppdatert med detaljerte grenser for Gruppe 1.
 */
object Iso15608Classifier {

    data class ClassificationResult(
        val groupName: String,
        val isCeRelevant: Boolean
    )

    fun classify(
        c: Double,
        mn: Double,
        cr: Double,
        mo: Double,
        v: Double,
        ni: Double,
        cu: Double,
        si: Double = 0.0,
        s: Double = 0.0,
        p: Double = 0.0,
        nb: Double = 0.0,
        ti: Double = 0.0
    ): ClassificationResult {
        // Rustfrie stål (Gruppe 7, 8, 10) - CE er ikke relevant her
        if (cr >= 10.5 && c <= 1.2) {
            val group = when {
                ni > 8.0 && ni < 35.0 && cr >= 16.0 -> "Gruppe 8 (Austenittisk)"
                ni <= 1.5 && cr >= 10.5 && cr <= 30.0 -> "Gruppe 7 (Ferritisk)"
                ni >= 1.5 && ni <= 5.0 && cr >= 10.5 && cr <= 13.5 -> "Gruppe 7 (Martensittisk)"
                ni >= 3.0 && ni <= 9.0 && cr >= 21.0 && cr <= 28.0 -> "Gruppe 10 (Duplex)"
                else -> "Gruppe 7/8/10 (Rustfritt)"
            }
            return ClassificationResult(group, isCeRelevant = false)
        }

        // Cr-Mo stål (Gruppe 4, 5, 6)
        if (cr > 0.3 || mo > 0.1) {
            val group = when {
                cr <= 0.75 && mo <= 0.7 -> "Gruppe 4"
                cr <= 1.5 && mo <= 0.6 -> "Gruppe 5"
                cr <= 10.0 && mo <= 1.2 -> "Gruppe 6"
                else -> "Gruppe 4/5/6 (Cr-Mo)"
            }
            return ClassificationResult(group, isCeRelevant = false)
        }

        // Detaljert sjekk for Gruppe 1 (Basert på brukerens bilde/tabell)
        // Grenser: C<=0.25, Si<=0.60, Mn<=1.8, Mo<=0.70, S<=0.045, P<=0.045, Cu<=0.40, Ni<=0.5, Cr<=0.3, Nb<=0.06, V<=0.1, Ti<=0.05
        val erGruppe1 = c <= 0.25 && 
                       si <= 0.60 && 
                       mn <= 1.8 && 
                       mo <= 0.70 && 
                       s <= 0.045 && 
                       p <= 0.045 && 
                       cu <= 0.40 && 
                       ni <= 0.5 && 
                       cr <= 0.3 && 
                       nb <= 0.06 && 
                       v <= 0.1 && 
                       ti <= 0.05

        if (erGruppe1) {
            return ClassificationResult("Gruppe 1", isCeRelevant = true)
        }

        // Karbonstål og finkornstål (Hvis ikke Gruppe 1, sjekk om det er Gruppe 2 eller 3)
        val group = when {
            c <= 0.25 && mn <= 1.8 -> "Gruppe 1 (Forenklet)" // Burde vært fanget opp av Gruppe 1 sjekken over hvis alle data var der
            c > 0.25 || mn > 1.8 -> "Gruppe 2"
            else -> "Gruppe 1/2/3"
        }
        return ClassificationResult(group, isCeRelevant = true)
    }
}
