package com.sveis.karbonekvivalent.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CarbonEquivalentCalculatorTest {

    @Test
    fun testCalculateCE() {
        // Sample data: C=0.2, Mn=0.6, Cr=0.2, Mo=0.1, V=0.05, Ni=0.3, Cu=0.15
        // CE = 0.2 + (0.6/6) + ((0.2 + 0.1 + 0.05)/5) + ((0.3 + 0.15)/15)
        // CE = 0.2 + 0.1 + (0.35/5) + (0.45/15)
        // CE = 0.2 + 0.1 + 0.07 + 0.03 = 0.40
        
        val result = CarbonEquivalentCalculator.calculateCE(
            carbon = 0.2,
            manganese = 0.6,
            chromium = 0.2,
            molybdenum = 0.1,
            vanadium = 0.05,
            nickel = 0.3,
            copper = 0.15
        )
        
        assertEquals(0.40, result, 0.0001)
    }

    @Test
    fun testEvaluateWeldabilityExcellent() {
        val ce = 0.35
        assertEquals(Weldability.EXCELLENT, CarbonEquivalentCalculator.evaluateWeldability(ce))
    }

    @Test
    fun testEvaluateWeldabilityGoodBoundary() {
        val ce = 0.36
        assertEquals(Weldability.GOOD, CarbonEquivalentCalculator.evaluateWeldability(ce))
    }

    @Test
    fun testEvaluateWeldabilityGood() {
        val ce = 0.50
        assertEquals(Weldability.GOOD, CarbonEquivalentCalculator.evaluateWeldability(ce))
    }

    @Test
    fun testEvaluateWeldabilityGoodUpperBoundary() {
        val ce = 0.55
        assertEquals(Weldability.GOOD, CarbonEquivalentCalculator.evaluateWeldability(ce))
    }

    @Test
    fun testEvaluateWeldabilityPoor() {
        val ce = 0.56
        assertEquals(Weldability.POOR, CarbonEquivalentCalculator.evaluateWeldability(ce))
    }
}
