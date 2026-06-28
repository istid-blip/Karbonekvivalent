package com.sveis.karbonekvivalent.util

import androidx.compose.runtime.Composable

/**
 * En plattform-spesifikk Composable som sørger for at skjermen holdes på
 * så lenge den er en del av UI-treet.
 */
@Composable
expect fun KeepScreenOn()
