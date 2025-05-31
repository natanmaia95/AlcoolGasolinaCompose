package com.example.alcoolgasolinacompose.view

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.alcoolgasolinacompose.GasStation
import com.example.alcoolgasolinacompose.editGasStationSharedFile


@Composable
fun EditGasStationDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    gasStation: GasStation,
    context: Context,
    onEditConfirmed: (GasStation) -> Unit
) {
    val modifier: Modifier = Modifier;
    var gasStationNameVar by remember { mutableStateOf(gasStation.name) }
    var gasolinePriceVar by remember { mutableStateOf(gasStation.priceGasoline) }
    var alcoholPriceVar by remember { mutableStateOf(gasStation.priceAlcohol) }

    Dialog(onDismissRequest = { onDismissRequest() })
    {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(25.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = gasStationNameVar,
                    onValueChange = {gasStationNameVar = it},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                TextField(
                    value = gasolinePriceVar.toString(),
                    onValueChange = {gasolinePriceVar = it.toFloat()},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = alcoholPriceVar.toString(),
                    onValueChange = {alcoholPriceVar = it.toFloat()},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Cancelar")
                    }
                    TextButton(
                        onClick = {
                            editGasStationSharedFile(
                                gasStation.id,
                                context,
                                "name",
                                gasStationNameVar
                            )
                            editGasStationSharedFile(
                                gasStation.id,
                                context,
                                "priceGasoline",
                                gasolinePriceVar.toString()
                            )
                            editGasStationSharedFile(
                                gasStation.id,
                                context,
                                "priceAlcohol",
                                alcoholPriceVar.toString()
                            )
                            val newGasStation = gasStation.copy(
                                name = gasStationNameVar,
                                priceAlcohol = alcoholPriceVar ,
                                priceGasoline = gasolinePriceVar
                            );
                            onEditConfirmed(newGasStation);
                            onConfirmation()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Salvar")
                    }
                }

            }
        }
    }
}
