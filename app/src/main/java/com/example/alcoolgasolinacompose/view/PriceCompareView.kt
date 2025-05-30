package com.example.alcoolgasolinacompose.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alcoolgasolinacompose.ui.theme.AlcoolGasolinaComposeTheme

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

    var precoGasolina by remember { mutableStateOf("") }
    var precoAlcool by remember { mutableStateOf("") }

    var deveEscolherGasolina by remember {
        mutableStateOf(false)

    }

    var use75 by remember {
        mutableStateOf(sharedPref.getBoolean(KEY_USE_75, false)) //false is default value
    }



    Column (
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

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

        Row (
//            modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Rendimento: " + if (use75) {"75%"} else {"70%"})
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

        Text(if (deveEscolherGasolina) {"MELHOR ESCOLHA: GASOLINA"} else {"MELHOR ESCOLHA: ALCOOL"})

        Button(
            onClick = {
                deveEscolherGasolina = calcDeveEscolherGasolina(precoGasolina.toFloat(), precoAlcool.toFloat(), use75)


            }
        ) {
            Text("Calcular o melhor combustível")
        }
    }

}


fun calcDeveEscolherGasolina(gasolina:Float, alcool:Float, use75: Boolean) : Boolean {
    var rate = 0.7
    if (use75) rate = 0.75
    return alcool/gasolina > rate
}