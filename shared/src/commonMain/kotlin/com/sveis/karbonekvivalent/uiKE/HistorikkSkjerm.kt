package com.sveis.karbonekvivalent.uiKE

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sveis.karbonekvivalent.db.CeEntry
import karbonekvivalent.shared.generated.resources.Res
import karbonekvivalent.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorikkSkjerm(
    entries: List<CeEntry>,
    language: String,
    onBack: () -> Unit,
    listState: LazyListState = rememberLazyListState(),
) {
    val useEdgeToEdge = LocalUseEdgeToEdge.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .then(if (useEdgeToEdge) Modifier.statusBarsPadding() else Modifier)
            .swipeToDismiss(
                onDismiss = onBack,
                swipeDirection = SwipeDirection.RIGHT
            )
    ) {
        AppHeader(
            tittel = stringResource(Res.string.history),
            venstreIkon = Icons.AutoMirrored.Filled.ArrowBack,
            venstreIkonBeskrivelse = stringResource(Res.string.back),
            onVenstreKlikk = onBack,
            tittelAlignment = Alignment.CenterEnd,
            tittelStil = MaterialTheme.typography.headlineSmall
        )

        if (entries.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Text(stringResource(Res.string.no_saved_calculations), style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(entries) { entry ->
                    HistoryItem(entry)
                }
            }
        }
    }

}

@Composable
fun HistoryItem(entry: CeEntry) {
    val displayTitle = if (!entry.name.isNullOrBlank()) {
        entry.name
    } else {
        "${stringResource(Res.string.calculation_prefix)} ${entry.id}"
    }

    StandardKort(tittel = displayTitle) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("CE: ${entry.ceResult.toString().take(5)}", style = MaterialTheme.typography.titleLarge)
                Text("C: ${entry.carbon}%, Mn: ${entry.manganese}%", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = formatTimestamp(entry.timestamp, stringResource(Res.string.timestamp_prefix)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

// Simple timestamp formatter for KMP
fun formatTimestamp(timestamp: Long, label: String): String {
    // This is a placeholder as full date formatting often requires platform specific code or a library
    return "$label: $timestamp"
}
