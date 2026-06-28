package com.sveis.karbonekvivalent.uiKE

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun VelgeContainer(
    visArk: Boolean,
    fraToppen: Boolean,
    modifier: Modifier = Modifier,
    ekstraPaddingBunn: androidx.compose.ui.unit.Dp = 0.dp,
    onLukkBehov: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val arkForm = RoundedCornerShape(20.dp)
    val vertikalAvstand = 80.dp

    val keyboardController = androidx.compose.ui.platform.LocalSoftwareKeyboardController.current
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    // Detekter om tastaturet er synlig og regn ut høyden i DP
    val insets = WindowInsets.ime
    val density = LocalDensity.current
    val imeBottomPx = insets.getBottom(density)
    val erTastaturApent = imeBottomPx > 0
    val imeBottomDp = with(density) { imeBottomPx.toDp() }

    // Magien ligger her: Kalkuler dynamisk bunn-padding
    val faktiskPaddingBunn = if (!fraToppen) {
        vertikalAvstand
    } else {
        // Velg den største verdien av enten dine paneler (ekstraPaddingBunn) eller tastaturet.
        // På denne måten unngår vi å komprimere vinduet i hjel med dobbel padding.
        val basePadding = vertikalAvstand + ekstraPaddingBunn
        val keyboardPadding = imeBottomDp + 16.dp
        if (basePadding > keyboardPadding) basePadding else keyboardPadding
    }

    LaunchedEffect(visArk) {
        if (visArk && !fraToppen) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }

    LaunchedEffect(erTastaturApent) {
        if (erTastaturApent && !fraToppen && visArk) {
            onLukkBehov?.invoke()
        }
    }

    AnimatedVisibility(
        visible = visArk && !(erTastaturApent && !fraToppen),
        enter = slideInVertically(animationSpec = tween(300)) { if (fraToppen) -it else it },
        exit = slideOutVertically(animationSpec = tween(300)) { if (fraToppen) -it else it }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(
                    top = if (fraToppen) vertikalAvstand else 0.dp,
                    bottom = faktiskPaddingBunn // <-- Bruker den dynamiske paddingen her
                )
                .then(modifier),
            contentAlignment = if (fraToppen) Alignment.TopCenter else Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    // Beholder den dynamiske høydebegrensningen din
                    .heightIn(max = if (fraToppen && (ekstraPaddingBunn > 0.dp)) 400.dp else 600.dp)
                    .drawBehind {
                        val shadowColor = Color.Black.copy(alpha = 0.9f)
                        repeat(10) { i ->
                            val iteration = i + 1
                            val spread = (iteration * 2.5).dp.toPx()
                            drawRoundRect(
                                color = shadowColor.copy(alpha = 0.24f / iteration),
                                topLeft = Offset(-spread, -spread),
                                size = Size(size.width + (spread * 2), size.height + (spread * 2)),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius((20 + iteration).dp.toPx())
                            )
                        }
                    }
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.9f),
                        shape = arkForm
                    ),
                shape = arkForm,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(remember { MutableInteractionSource() }, null) {}
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                }
            }
        }
    }
}
