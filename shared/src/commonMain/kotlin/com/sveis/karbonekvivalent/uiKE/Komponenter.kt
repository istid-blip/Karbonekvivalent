package com.sveis.karbonekvivalent.uiKE

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import com.sveis.karbonekvivalent.KalkuleringsMetode
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Modifier.swipeToDismiss(
    onDismiss: () -> Unit,
    minSwipeDistance: Float = 150f,
    swipeDirection: SwipeDirection
): Modifier = this.pointerInput(Unit) {
    detectHorizontalDragGestures { change, dragAmount ->
        change.consume()
        when (swipeDirection) {
            SwipeDirection.LEFT -> if (dragAmount < -minSwipeDistance) onDismiss()
            SwipeDirection.RIGHT -> if (dragAmount > minSwipeDistance) onDismiss()
        }
    }
}

enum class SwipeDirection {
    LEFT, RIGHT
}

@Composable
fun StandardKort(
    tittel: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val isRetro = HeatInputTheme.current == AppThemeType.RETRO
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = if (isRetro) RoundedCornerShape(0.dp) else MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isRetro) tittel.uppercase() else tittel,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun AutoResizedText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Center,
    maxLines: Int = 1,
) {
    var resizedTextStyle by remember(style) { mutableStateOf(style) }
    var readyToDraw by remember(text, style) { mutableStateOf(value = false) }

    Text(
        text = text,
        color = color,
        textAlign = textAlign,
        modifier = modifier.drawWithContent {
            if (readyToDraw) drawContent()
        },
        style = resizedTextStyle,
        softWrap = false,
        maxLines = maxLines,
        onTextLayout = { result ->
            if (result.didOverflowWidth || result.didOverflowHeight) {
                if (resizedTextStyle.fontSize.value > 7f) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = resizedTextStyle.fontSize * 0.92,
                    )
                } else {
                    readyToDraw = true
                }
            } else {
                readyToDraw = true
            }
        },
    )
}

@Composable
fun AnimerbartInputFelt(
    verdi: String,
    label: String,
    enhet: String,
    erAktiv: Boolean,
    erNoeAktivt: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val alpha by animateFloatAsState(
        targetValue = if (erNoeAktivt && !erAktiv) 0.4f else 1.0f,
        label = "inputAlpha"
    )

    Box(
        modifier = modifier
            .graphicsLayer { this.alpha = alpha }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 8.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp),
            shape = MaterialTheme.shapes.small,
            border = BorderStroke(
                width = if (erAktiv) 2.dp else 1.dp,
                color = MaterialTheme.colorScheme.primary
            ),
            color = if (erAktiv) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = verdi,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 26.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        AutoResizedText(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
            color = if (erAktiv) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 8.dp, y = (-8).dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 4.dp),
            textAlign = TextAlign.Start
        )

        Text(
            text = enhet,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
            color = if (erAktiv) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-8).dp, y = 8.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun InnstillingValgKort(
    tittel: String,
    hovedtekst: String,
    undertekst: String,
    infoTekst: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    erUtvidet: Boolean = false,
    ekstraInnhold: (@Composable ColumnScope.() -> Unit)? = null
) {
    val isRetro = HeatInputTheme.current == AppThemeType.RETRO
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val primaryColor = MaterialTheme.colorScheme.primary

    Box(modifier = modifier.padding(vertical = 8.dp)) {
        // Ytre ramme (instrumentpanelet)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .then(
                    if (isRetro) {
                        Modifier.drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            val dashWidth = 8.dp.toPx()
                            val gapWidth = 4.dp.toPx()
                            drawRect(
                                color = primaryColor.copy(alpha = 0.25f),
                                style = Stroke(
                                    width = strokeWidth,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidth, gapWidth), 0f)
                                )
                            )
                        }
                    } else Modifier
                ),
            shape = if (isRetro) RoundedCornerShape(0.dp) else MaterialTheme.shapes.medium,
            color = if (isRetro) primaryColor.copy(alpha = 0.03f) else Color.Transparent,
            border = if (isRetro) null else BorderStroke(1.dp, outlineColor)
        ) {
            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                // Selve knappen inne i rammen
                InnstillingKlikkFelt(
                    hovedtekst = hovedtekst,
                    undertekst = undertekst,
                    onClick = onClick,
                    erUtvidbar = ekstraInnhold != null,
                    erUtvidet = erUtvidet,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp)
                )

                // Ekstra innhold (utvidbart felt)
                AnimatedVisibility(
                    visible = erUtvidet && ekstraInnhold != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        ekstraInnhold?.invoke(this)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // Informasjonsfelt under knappen
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = primaryColor.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = infoTekst,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isRetro) primaryColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Informasjonsfeltet ("tittelen") som bryter rammelinjen
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = if (isRetro) " ${tittel.uppercase()} " else " $tittel ",
                style = MaterialTheme.typography.labelSmall,
                color = primaryColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun InnstillingKlikkFelt(
    hovedtekst: String,
    undertekst: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    erUtvidbar: Boolean = false,
    erUtvidet: Boolean = false
) {
    val isRetro = HeatInputTheme.current == AppThemeType.RETRO
    val primaryColor = MaterialTheme.colorScheme.primary
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0.7f,
        label = "klikkFeltAlpha"
    )

    val rotation by animateFloatAsState(
        targetValue = if (erUtvidet) 180f else 0f,
        label = "rotation"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = if (isRetro) RoundedCornerShape(0.dp) else MaterialTheme.shapes.small,
        color = if (isRetro) {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f * alpha)
        },
        border = BorderStroke(1.dp, primaryColor.copy(alpha = 0.8f * alpha))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isRetro) hovedtekst.uppercase() else hovedtekst,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        letterSpacing = if (isRetro) 0.5.sp else TextUnit.Unspecified
                    ),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isRetro) undertekst.uppercase() else undertekst,
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = if (isRetro) 0.3.sp else TextUnit.Unspecified
                    ),
                    color = if (isRetro) primaryColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Icon(
                imageVector = if (erUtvidbar) Icons.Default.ArrowDropDown else Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = primaryColor.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(if (erUtvidbar) 28.dp else 20.dp)
                    .graphicsLayer { rotationZ = rotation }
            )
        }
    }
}

@Composable
fun <T> AppSwitcher(
    selected: T,
    options: List<T>,
    onSelected: (T) -> Unit,
    labelProvider: (T) -> String,
    modifier: Modifier = Modifier
) {
    val isRetro = HeatInputTheme.current == AppThemeType.RETRO
    val primaryColor = MaterialTheme.colorScheme.primary
    val dimColor = if (isRetro) Color(0xFF004400) else MaterialTheme.colorScheme.outlineVariant
    val shape = if (isRetro) RoundedCornerShape(0.dp) else RoundedCornerShape(12.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = if (isRetro) 1.5.dp else 1.dp,
                color = dimColor,
                shape = shape
            )
            .clip(shape)
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            val haptic = LocalHapticFeedback.current
            
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) primaryColor else Color.Transparent,
                animationSpec = tween(durationMillis = 200),
                label = "BgColor"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) {
                    if (isRetro) Color.Black else MaterialTheme.colorScheme.onPrimary
                } else {
                    if (isRetro) Color(0xFF004400) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                },
                animationSpec = tween(durationMillis = 200),
                label = "TextColor"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(backgroundColor)
                    .clickable { 
                        if (!isSelected) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) 
                            onSelected(option) 
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = labelProvider(option),
                    color = textColor,
                    fontFamily = if (isRetro) FontFamily.Monospace else FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun HovedSkjermHeader(
    onApneInnstillinger: () -> Unit,
    onApneHistorikk: () -> Unit,
    language: String,
    dimmet: Boolean = false,
    kalkuleringsMetode: KalkuleringsMetode = KalkuleringsMetode.IIW
) {
    val alpha by animateFloatAsState(if (dimmet) 0.4f else 1.0f, label = "headerAlpha")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(48.dp)
            .graphicsLayer { this.alpha = alpha }
    ) {
        IconButton(
            onClick = onApneInnstillinger,
            modifier = Modifier.align(Alignment.CenterStart),
            enabled = !dimmet
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = if (language == "no") "Innstillinger" else "Settings",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(32.dp)
            )
        }

        val tittel = when (kalkuleringsMetode) {
            KalkuleringsMetode.IIW -> if (language == "no") "CE (IIW)" else "CE (IIW)"
        }

        AutoResizedText(
            text = tittel.uppercase(),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(
            onClick = onApneHistorikk,
            modifier = Modifier.align(Alignment.CenterEnd),
            enabled = !dimmet
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = if (language == "no") "Historikk" else "History",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
