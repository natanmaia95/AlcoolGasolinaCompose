package com.example.alcoolgasolinacompose

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONObject

fun openNumberOfGasStationFile(context: Context) : Int
{
    val sharedFileName = "number_of_gas_stations";
    val key = "number";
    var numberOfGasStations: Int = 0;
    var sp : SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE);
    if (!sp.contains(key)) {
        val editor = sp.edit();
        editor.putInt(key, numberOfGasStations);
        editor.apply();
    } else {
        numberOfGasStations = sp.getInt(key, 0);
    }
    return numberOfGasStations;
}

fun saveGasStationNumber(newValue: Int, context: Context)
{
    val sharedFileName = "number_of_gas_stations";
    val key = "number";
    val sp : SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE);
    sp.edit().putInt(key, newValue).apply();
}

fun createJsonObjectFromGasStationClass(gasStation: GasStation) : JSONObject
{
    return JSONObject().apply {
        put("id", gasStation.id);
        put("name", gasStation.name);
        put("latitude", gasStation.latidute);
        put("longitude", gasStation.longitude);
        put("priceGasoline", gasStation.priceGasoline);
        put("priceAlcohol", gasStation.priceAlcohol);
    }
}

fun createGasStationFromJson(json: JSONObject) : GasStation
{
    return GasStation(
        json?.optInt("id", 0) ?: 0,
        json?.optString("name", "") ?: "",
        json?.optString("latitude", "" ) ?: "",
        json?.optString("longitude", "" ) ?: "",
        json?.optDouble("priceGasoline", 0.0 )?.toFloat() ?: 0.0f,
        json?.optDouble("priceAlcohol", 0.0 )?.toFloat() ?: 0.0f,
    )
}

fun deleteGasStationSharedFile(id: Int, context: Context): Boolean
{
    return context.deleteSharedPreferences("GasStation$id");
}

fun readGasStationFromFile(id: Int, context: Context) : String
{
    val sp : SharedPreferences = context.getSharedPreferences("GasStation$id", Context.MODE_PRIVATE);
    return  sp.getString("station", "").toString();
}

fun saveGasStationJsonObjectInFile(
    id: Int,
    context: Context,
    gasStationJson: JSONObject
) {
    val sp : SharedPreferences = context.getSharedPreferences("GasStation$id", Context.MODE_PRIVATE);
    val editor = sp.edit();
    editor.putString("station", gasStationJson.toString())
    editor.apply();
}


fun editGasStationSharedFile(id: Int, context: Context, key: String, newValue: String)
{
    val gasStationJsonObject : JSONObject = JSONObject(readGasStationFromFile(id, context));
    if (key == "priceGasoline" || key == "priceAlcohol") {
        gasStationJsonObject.apply {
            put(key, newValue.toFloat());
        }
    }else {
        gasStationJsonObject.apply {
            put(key, newValue);
        }
    }
    saveGasStationJsonObjectInFile(id, context, gasStationJsonObject)
}

fun readGasStationsListFromFile(context: Context) : ArrayList<GasStation>
{
    val listOfGasStations: ArrayList<GasStation> = ArrayList();
    var numberOfGasStations = openNumberOfGasStationFile(context);

    while (numberOfGasStations >= 1)
    {
        try {
            val gasStationJson = JSONObject(readGasStationFromFile(numberOfGasStations-1, context));
            listOfGasStations.add(
                GasStation(
                    gasStationJson.optInt("id", 0) ?: 0,
                    gasStationJson.optString("name", "") ?: "",
                    gasStationJson.optString("latitude", "" ) ?: "",
                    gasStationJson.optString("longitude", "" ) ?: "",
                    gasStationJson.optDouble("priceGasoline", 0.0 ).toFloat() ?: 0.0f,
                    gasStationJson.optDouble("priceAlcohol", 0.0 ).toFloat() ?: 0.0f,
                )
            )
            Log.e("ReadGasStation", "Id ${gasStationJson.optString("name", "") ?: ""}")

            numberOfGasStations -= 1;
        }catch (e: Exception) {
            Log.e("ReadGasStation", "Erro ao ler arquivo gasStation${numberOfGasStations - 1}: ${e.message}")

        }

    }

    return listOfGasStations;
    val gasStations = arrayListOf<GasStation>()
    val files = context.fileList()



    return gasStations
}