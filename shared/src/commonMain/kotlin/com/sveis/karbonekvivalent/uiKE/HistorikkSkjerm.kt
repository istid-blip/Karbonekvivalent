package com.sveis.karbonekvivalent.uiKE

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sveis.karbonekvivalent.db.CeEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorikkSkjerm(
    entries: List<CeEntry>,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.swipeToDismiss(
            onDismiss = onBack,
            swipeDirection = SwipeDirection.RIGHT
        ),
        topBar = {
            AppHeader(
                tittel = "Historikk",
                venstreIkon = Icons.AutoMirrored.Filled.ArrowBack,
                venstreIkonBeskrivelse = "Tilbake",
                onVenstreKlikk = onBack,
                tittelAlignment = Alignment.CenterEnd,
                tittelStil = MaterialTheme.typography.headlineSmall
            )
        }
    ) { padding ->
        if (entries.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Ingen lagrede beregninger", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
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
    StandardKort(tittel = "Beregning ${entry.id}") {
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
                text = formatTimestamp(entry.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

// Simple timestamp formatter for KMP
fun formatTimestamp(timestamp: Long): String {
    // This is a placeholder as full date formatting often requires platform specific code or a library
    return "Tidspunkt: $timestamp"
}
