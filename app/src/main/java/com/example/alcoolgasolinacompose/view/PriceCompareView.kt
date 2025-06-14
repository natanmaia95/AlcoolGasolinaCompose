package com.example.alcoolgasolinacompose.view

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.alcoolgasolinacompose.ui.theme.AlcoolGasolinaComposeTheme
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.res.stringResource
import com.example.alcoolgasolinacompose.GasStation
import com.example.alcoolgasolinacompose.R
import com.example.alcoolgasolinacompose.openNumberOfGasStationFile
import com.example.alcoolgasolinacompose.createJsonObjectFromGasStationClass
import com.example.alcoolgasolinacompose.saveGasStationJsonObjectInFile
import com.example.alcoolgasolinacompose.saveGasStationNumber

import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

private const val SHARED_PREF_NAME = "NAME"
private const val KEY_USE_75 = "use75"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceCompareView(navController: NavController, savedInstanceState: Bundle?) {
    val modifier:Modifier = Modifier
    val context = LocalContext.current;
    val sharedPref: SharedPreferences = remember {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context);
    }
    var numberGas = 0;
    var precoGasolina by remember { mutableStateOf("") }
    var precoAlcool by remember { mutableStateOf("") }
    var gasStationName by remember { mutableStateOf("") }
    var deveEscolherGasolina by remember {
        mutableStateOf(false)
    }
    var use75 by remember {
        mutableStateOf(sharedPref.getBoolean(KEY_USE_75, false)) //false is default value
    }

    var showAlertDialog by remember { mutableStateOf(false) }
//    var alertDialogText by remember { mutableStateOf("") }
    var alertDialogStringId by remember { mutableStateOf(R.string.alert_localizacao_indisponivel) }

    numberGas = openNumberOfGasStationFile(context);

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                lateinit var lat: String;
                lateinit var lng: String;
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).addOnSuccessListener { location ->
                    location?.let {
                        lat = it.latitude.toString();
                        lng = it.longitude.toString();

                        val gasStation = GasStation(
                            numberGas,
                            gasStationName,
                            lat,
                            lng,
                            precoGasolina.toFloat(),
                            precoAlcool.toFloat()
                        )
                        val gasStationJson = createJsonObjectFromGasStationClass(gasStation);
                        saveGasStationJsonObjectInFile(numberGas, context, gasStationJson);
                        numberGas += 1;
                        saveGasStationNumber(numberGas, context);
//                        alertDialogText = stringResource(R.string.alert_posto_salvo);
                        alertDialogStringId = R.string.alert_posto_salvo
                        showAlertDialog = true;
                    } ?: run {
//                        alertDialogText = stringResource(R.string.alert_localizacao_indisponivel);
                        alertDialogStringId = R.string.alert_localizacao_indisponivel
                        showAlertDialog = true;
                    }

                }

            }
        } else {
//            alertDialogText = stringResource(R.string.alert_localizacao_negada);
            alertDialogStringId = R.string.alert_localizacao_negada
            showAlertDialog = true;
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column (
            modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            TextField(
                value = precoGasolina,
                label = { Text(stringResource(id = R.string.entry_preco_gasolina)) },
                onValueChange = {precoGasolina = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(Modifier.height(16.dp))


            TextField(
                value = precoAlcool,
                label = { Text(stringResource(id = R.string.entry_preco_alcool)) },
                onValueChange = {precoAlcool = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(Modifier.height(16.dp))


            TextField(
                value = gasStationName,
                label = { Text(stringResource(id = R.string.entry_nome_posto)) },
                onValueChange = {gasStationName = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Row (
//            modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.switch_rendimento) + " " + if (use75) {"75%"} else {"70%"})
                Switch(
                    checked = use75,
                    onCheckedChange = {
                        use75 = it
                        //salvar
                        with (sharedPref.edit()) {
                            putBoolean(KEY_USE_75, use75)
                            apply()
                        }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                stringResource(R.string.calculo_melhorescolha) + " " +
                if (deveEscolherGasolina) {stringResource(R.string.calculo_gasolina)} else {stringResource(R.string.calculo_alcool)}
            )

            Button(
                onClick = { deveEscolherGasolina = calcDeveEscolherGasolina(precoGasolina.toFloat(), precoAlcool.toFloat(), use75) }
            ) {
                Text(stringResource(R.string.calculo_botao_calcular))
            }

            Button(
                onClick = { navController.navigate("listGasStations"); }
            ) {
                Text(stringResource(R.string.calculo_botao_listapostos))
            }
        }

        FloatingActionButton(
            onClick = {
                if (
                    gasStationName != "" &&
                    precoAlcool != "" &&
                    precoGasolina != ""
                ) {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp, bottom = 48.dp),
            containerColor = Color(0xFF00BCD4)
        ) {
            Icon(Icons.Filled.Add, "Small floating action button to add gasStation.")
        }
    }

    if(showAlertDialog) {
        AlertDialog(
            title = { Text(stringResource(R.string.calculo_dialogue_salvarposto)) },
            text = { Text(stringResource(alertDialogStringId)) },
            onDismissRequest = {

            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAlertDialog = false
                    }
                ) {
                    Text(stringResource(R.string.alert_exit_ok))
                }
            },
        )
    }



}


fun calcDeveEscolherGasolina(gasolina:Float, alcool:Float, use75: Boolean) : Boolean {
    var rate = 0.7
    if (use75) rate = 0.75
    return alcool/gasolina > rate
}