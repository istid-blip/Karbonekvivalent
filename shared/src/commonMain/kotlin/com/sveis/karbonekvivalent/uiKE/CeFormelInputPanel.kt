package com.sveis.karbonekvivalent.uiKE

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CeFormelInputPanel(
    carbon: Double,
    manganese: Double,
    chromium: Double,
    molybdenum: Double,
    vanadium: Double,
    nickel: Double,
    copper: Double,
    aktivtElement: String?,
    onElementClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val erNoeAktivt = aktivtElement != null
    val scrollState = rememberScrollState()
    val operatorStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
    val nevnerStyle = MaterialTheme.typography.bodyLarge.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // CE =
        Text(text = "CE =", style = operatorStyle)

        // C
        AnimerbartInputFelt(
            verdi = carbon.toString(),
            label = "C",
            enhet = "%",
            erAktiv = aktivtElement == "C",
            erNoeAktivt = erNoeAktivt,
            onClick = { onElementClick("C") },
            modifier = Modifier.width(70.dp)
        )

        // +
        Text(text = "+", style = operatorStyle)

        // Mn / 6
        BrukSoyle(
            teller = {
                AnimerbartInputFelt(
                    verdi = manganese.toString(),
                    label = "Mn",
                    enhet = "%",
                    erAktiv = aktivtElement == "Mn",
                    erNoeAktivt = erNoeAktivt,
                    onClick = { onElementClick("Mn") },
                    modifier = Modifier.width(70.dp)
                )
            },
            nevner = "6",
            nevnerStyle = nevnerStyle
        )

        // +
        Text(text = "+", style = operatorStyle)

        // (Cr + Mo + V) / 5
        BrukSoyle(
            teller = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "(", style = operatorStyle)
                    AnimerbartInputFelt(
                        verdi = chromium.toString(),
                        label = "Cr",
                        enhet = "%",
                        erAktiv = aktivtElement == "Cr",
                        erNoeAktivt = erNoeAktivt,
                        onClick = { onElementClick("Cr") },
                        modifier = Modifier.width(70.dp)
                    )
                    Text(text = "+", style = operatorStyle.copy(fontSize = 16.sp))
                    AnimerbartInputFelt(
                        verdi = molybdenum.toString(),
                        label = "Mo",
                        enhet = "%",
                        erAktiv = aktivtElement == "Mo",
                        erNoeAktivt = erNoeAktivt,
                        onClick = { onElementClick("Mo") },
                        modifier = Modifier.width(70.dp)
                    )
                    Text(text = "+", style = operatorStyle.copy(fontSize = 16.sp))
                    AnimerbartInputFelt(
                        verdi = vanadium.toString(),
                        label = "V",
                        enhet = "%",
                        erAktiv = aktivtElement == "V",
                        erNoeAktivt = erNoeAktivt,
                        onClick = { onElementClick("V") },
                        modifier = Modifier.width(70.dp)
                    )
                    Text(text = ")", style = operatorStyle)
                }
            },
            nevner = "5",
            nevnerStyle = nevnerStyle
        )

        // +
        Text(text = "+", style = operatorStyle)

        // (Ni + Cu) / 15
        BrukSoyle(
            teller = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "(", style = operatorStyle)
                    AnimerbartInputFelt(
                        verdi = nickel.toString(),
                        label = "Ni",
                        enhet = "%",
                        erAktiv = aktivtElement == "Ni",
                        erNoeAktivt = erNoeAktivt,
                        onClick = { onElementClick("Ni") },
                        modifier = Modifier.width(70.dp)
                    )
                    Text(text = "+", style = operatorStyle.copy(fontSize = 16.sp))
                    AnimerbartInputFelt(
                        verdi = copper.toString(),
                        label = "Cu",
                        enhet = "%",
                        erAktiv = aktivtElement == "Cu",
                        erNoeAktivt = erNoeAktivt,
                        onClick = { onElementClick("Cu") },
                        modifier = Modifier.width(70.dp)
                    )
                    Text(text = ")", style = operatorStyle)
                }
            },
            nevner = "15",
            nevnerStyle = nevnerStyle
        )
    }
}

@Composable
private fun BrukSoyle(
    teller: @Composable () -> Unit,
    nevner: String,
    nevnerStyle: androidx.compose.ui.text.TextStyle
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        teller()
        HorizontalDivider(
            modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max).padding(vertical = 4.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(text = nevner, style = nevnerStyle)
    }
}
