package com.example.alcoolgasolinacompose.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alcoolgasolinacompose.ui.theme.AlcoolGasolinaComposeTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceCompareView(navController: NavController, savedInstanceState: Bundle?) {

    val modifier:Modifier = Modifier

    var precoGasolina by remember { mutableStateOf("") }
    var precoAlcool by remember { mutableStateOf("") }

    var deveEscolherGasolina by remember { mutableStateOf(false) }

    Column (modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        TextField(
            value = precoGasolina,
            label = { Text("Preço da Gasolina") },
            onValueChange = {precoGasolina = it},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        TextField(
            value = precoAlcool,
            label = { Text("Preço do Álcool") },
            onValueChange = {precoAlcool = it},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(Modifier.height(16.dp))

        Text(if (deveEscolherGasolina) {"GASOLINA"} else {"ALCOOL"})

        Button(
            onClick = { deveEscolherGasolina = calcDeveEscolherGasolina(precoGasolina.toFloat(), precoAlcool.toFloat())}
        ) {
            Text("Calcular o melhor combustível")
        }
    }
}


fun calcDeveEscolherGasolina(gasolina:Float, alcool:Float) : Boolean {
    return alcool/gasolina > 0.7
}
