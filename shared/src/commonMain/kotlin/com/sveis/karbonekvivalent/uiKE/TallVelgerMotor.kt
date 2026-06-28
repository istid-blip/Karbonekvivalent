package com.sveis.karbonekvivalent.uiKE

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue
import kotlin.math.round
import kotlin.math.roundToInt
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

@Composable
fun TallVelgerMotor(
    label: String,
    verdi: Double,
    onVerdiChange: (Double) -> Unit,
    steg: Double = 0.01,
    minVerdi: Double = 0.0,
    maksVerdi: Double = 2.0,
    modifier: Modifier = Modifier,
    isKompakt: Boolean = false
) {
    val isRetro = HeatInputTheme.current == AppThemeType.RETRO
    
    Column(
        modifier = modifier
            .padding(vertical = if (isKompakt) 2.dp else 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isRetro) label.uppercase() else label,
            style = if (isKompakt) MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp) else MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            maxLines = 1
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isKompakt) 80.dp else 120.dp),
            contentAlignment = Alignment.Center
        ) {
            TallVelgerHjul(
                startVerdi = verdi,
                minVerdi = minVerdi,
                maksVerdi = maksVerdi,
                steg = steg,
                onVerdiValgt = onVerdiChange,
                isKompakt = isKompakt
            )
        }
    }
}

@Composable
fun TallVelgerHjul(
    startVerdi: Double,
    minVerdi: Double,
    maksVerdi: Double,
    steg: Double,
    onVerdiValgt: (Double) -> Unit,
    isKompakt: Boolean = false
) {
    val elementHoyde = if (isKompakt) 14.dp else 20.dp
    val hjulHoyde = if (isKompakt) 80.dp else 120.dp
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val antallElementer = ((maksVerdi - minVerdi) / steg).roundToInt() + 1
    val startIndeks = ((startVerdi - minVerdi) / steg).roundToInt().coerceIn(0, antallElementer - 1)
    
    val pagerState = rememberPagerState(
        initialPage = startIndeks,
        pageCount = { antallElementer },
    )

    val stil = HeatInputTheme.tallVelgerStyle
    var harInteragert by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        if (harInteragert) {
            val aktuellVerdi = minVerdi + (pagerState.currentPage * steg)
            val avrundetVerdi = round(aktuellVerdi * 100.0) / 100.0
            onVerdiValgt(avrundetVerdi)
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(hjulHoyde),
        contentAlignment = Alignment.Center
    ) {
        VerticalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(elementHoyde),
            userScrollEnabled = false,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragStart = { harInteragert = true },
                        onDragEnd = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage)
                            }
                        },
                        onDragCancel = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage)
                            }
                        },
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            val grunnFolsomhet = 0.5f
                            val akselerasjon = 1f + (kotlin.math.abs(dragAmount) / 10f)
                            val forsterketDrag = (dragAmount * grunnFolsomhet) * akselerasjon
                            pagerState.dispatchRawDelta(-forsterketDrag)
                        }
                    )
                },
            contentPadding = PaddingValues(vertical = (hjulHoyde / 2) - (elementHoyde / 2))
        ) { index ->
            val aktuellVerdi = minVerdi + (index * steg)
            val avstandFraMidten = ((pagerState.currentPage - index) + pagerState.currentPageOffsetFraction).absoluteValue

            val strekType = when {
                steg < 1.0 -> {
                    val intVerdi = (aktuellVerdi * 100).roundToInt()
                    when {
                        (intVerdi % 10 == 0) -> if (isKompakt) TallVelgerStrekType.MEDIUM else TallVelgerStrekType.STOR
                        (intVerdi % 5 == 0) -> if (isKompakt) TallVelgerStrekType.LITEN else TallVelgerStrekType.MEDIUM
                        else -> TallVelgerStrekType.LITEN
                    }
                }
                else -> {
                    val intVerdi = aktuellVerdi.roundToInt()
                    when {
                        (intVerdi % 10 == 0) -> if (isKompakt) TallVelgerStrekType.MEDIUM else TallVelgerStrekType.STOR
                        (intVerdi % 5 == 0) -> if (isKompakt) TallVelgerStrekType.LITEN else TallVelgerStrekType.MEDIUM
                        else -> TallVelgerStrekType.LITEN
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(elementHoyde)
                    .graphicsLayer {
                        alpha = (1f - (avstandFraMidten * 0.2f)).coerceIn(0.2f, 1f)
                        val scale = (1f - (avstandFraMidten * (if (isKompakt) 0.08f else 0.05f))).coerceIn(0.80f, 1f)
                        scaleX = scale
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(if (isKompakt) 1.dp else 2.dp)
                        .width(if (isKompakt) strekType.bredde * 0.7f else strekType.bredde)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(1.dp)
                        )
                )
            }
        }

        // Overlay marker
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isKompakt) 16.dp else 24.dp)
                .drawBehind {
                    drawLine(
                        color = Color.White.copy(alpha = 0.1f),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .background(
                    brush = Brush.verticalGradient(
                        0.0f to stil.glassGjenskinnFarge.copy(alpha = stil.glassAlphaKanter),
                        0.5f to stil.glassGjenskinnFarge.copy(alpha = stil.glassAlphaSenter),
                        1.0f to stil.glassGjenskinnFarge.copy(alpha = stil.glassAlphaKanter)
                    )
                )
        )
    }
}

enum class TallVelgerStrekType(val bredde: androidx.compose.ui.unit.Dp) {
    LITEN(40.dp),
    MEDIUM(70.dp),
    STOR(100.dp)
}
