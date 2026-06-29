package com.sveis.karbonekvivalent.util

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

/**
 * Samler alle rulle-tilstander for applikasjonen i én pakke for enklere håndtering og vedlikehold.
 */
@Stable
data class AppScrollStates(
    val homeScrollState: ScrollState,
    val formulaScrollState: ScrollState,
    val historyListState: LazyListState,
    val settingsScrollState: ScrollState
)

/**
 * Oppretter og husker alle rulle-tilstander på ett sted.
 */
@Composable
fun rememberAppScrollStates(): AppScrollStates {
    return AppScrollStates(
        homeScrollState = rememberScrollState(),
        formulaScrollState = rememberScrollState(),
        historyListState = rememberLazyListState(),
        settingsScrollState = rememberScrollState()
    )
}
