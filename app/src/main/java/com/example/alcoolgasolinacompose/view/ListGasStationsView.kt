package com.example.alcoolgasolinacompose.view

import android.content.Intent
import android.net.Uri

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.alcoolgasolinacompose.GasStation
import com.example.alcoolgasolinacompose.deleteGasStationSharedFile
import com.example.alcoolgasolinacompose.openNumberOfGasStationFile
import com.example.alcoolgasolinacompose.readGasStationsListFromFile
import com.example.alcoolgasolinacompose.saveGasStationNumber


@Composable
fun ListGasStationView()
{
    val modifier: Modifier = Modifier;
    val context = LocalContext.current;

    var showDeleteDialogConfirmation by remember { mutableStateOf(false) }
    var deleteDialogText by remember { mutableStateOf("") }
    val listOfGasStations = remember { mutableStateListOf<GasStation>() }
    var numberOfGasStation by remember { mutableStateOf(0) }
    numberOfGasStation = openNumberOfGasStationFile(context);
    LaunchedEffect(Unit) {
        listOfGasStations.clear()
        listOfGasStations.addAll(readGasStationsListFromFile(context))
    }
    var showEditDialog by remember { mutableStateOf(false) }
    var gasStationToDialog by remember { mutableStateOf(GasStation(
        0,
        "",
        "",
        "",
        0.0f,
        0.0f
    )
    )
    }



    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1419))
            .padding(start = 16.dp, end = 16.dp, top = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        listOfGasStations.forEach { gasStation ->
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color.Black)
                    .clickable { },
                shape = RoundedCornerShape(10.dp),
            ) {
                Column (modifier.padding(5.dp)) {
                    Text(text = gasStation.name);
                    Spacer(Modifier.height(3.dp));
                    Text(text = "Preço gasolina: ${gasStation.priceGasoline}");
                    Spacer(Modifier.height(3.dp));
                    Text(text = "Preço Alcohol: ${gasStation.priceAlcohol}");
                    Spacer(Modifier.height(3.dp));
                    TextButton(
                        onClick = {
                            val gmmIntentUri = Uri.parse(
                                "geo:${gasStation.latidute},${gasStation.longitude}"
                            )
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                                setPackage("com.google.android.apps.maps")
                            }
                            context.startActivity(mapIntent)
                        }
                    ){
                        Text(text = "Ver no mapa")
                    }
                    Row {
                        TextButton(
                            onClick = {
                                gasStationToDialog = gasStation
                                showEditDialog = true;
                            }
                        ) {
                            Text(text = "Editar")
                        }
                        TextButton(
                            onClick = {
                                val wasDeleteSuccesful = deleteGasStationSharedFile(gasStation.id, context);
                                if(wasDeleteSuccesful){
                                    numberOfGasStation -= 1;
                                    saveGasStationNumber(numberOfGasStation, context);
                                    val elementIndex = listOfGasStations.indexOfFirst { element -> element.id == gasStation.id };
                                    if(elementIndex > -1) {
                                        listOfGasStations.removeAt(elementIndex)
                                    }
                                    deleteDialogText = "Posto deletado com sucesso";
                                }else{
                                    deleteDialogText = "Ocorreu um erro, tente mais tarde";
                                }
                                showDeleteDialogConfirmation = true;
                            }
                        ) {
                            Text(text = "Deletar")
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        EditGasStationDialog(
            onDismissRequest = {showEditDialog = false},
            onConfirmation = {showEditDialog = false},
            gasStationToDialog,
            context,
            onEditConfirmed = { gasStationEdited ->
                val gasStationIndex = listOfGasStations.indexOfFirst {
                        element -> element.id == gasStationEdited.id
                }
                if(gasStationIndex != -1) {
                    listOfGasStations[gasStationIndex] = gasStationEdited;
                }
            }
        )
    }

    if(showDeleteDialogConfirmation) {
        AlertDialog(
            title = {
                Text(text = "Deletar posto")
            },
            text = {
                Text(text = deleteDialogText)
            },
            onDismissRequest = {

            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialogConfirmation = false
                    }
                ) {
                    Text("Confirm")
                }
            },
        )
    }
}