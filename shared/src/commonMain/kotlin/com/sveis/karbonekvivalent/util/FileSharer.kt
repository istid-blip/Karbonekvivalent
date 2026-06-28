package com.sveis.karbonekvivalent.util

/**
 * Plattform-spesifikk funksjon for å dele en tekstfil (JSON) via share sheet.
 */
expect fun shareBackupFile(jsonContent: String, fileName: String)

/**
 * Plattform-spesifikk funksjon for å lagre en tekstfil (JSON) til disk.
 */
expect fun saveBackupFile(jsonContent: String, fileName: String)
