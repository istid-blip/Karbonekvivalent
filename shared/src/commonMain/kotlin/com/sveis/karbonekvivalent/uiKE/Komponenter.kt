package com.sveis.karbonekvivalent.uiKE

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.ui.geometry.Size

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
