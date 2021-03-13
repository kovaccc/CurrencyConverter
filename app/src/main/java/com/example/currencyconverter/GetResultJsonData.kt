package com.example.currencyconverter

import android.util.Log
import com.example.currencyconverter.model.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException


private const val TAG = "GetResultJsonData"
class GetResultJsonData { // use this class for parsing JSON response since response has String values I won't use GsonConverter

    suspend fun parseJsonData(data : String) : ArrayList<Currency> {
        Log.d(TAG, "parseJsonData starts with $data")
        val currencyList = ArrayList<Currency>()

        withContext(Dispatchers.Default) { //  Default dispatcher for json parsing
            try {
                val jsonData = JSONArray(data)

                for (i in 0 until jsonData.length()) {
                    val jsonCurrency = jsonData.getJSONObject(i)

                    val medianRate = jsonCurrency.getString("median_rate")
                    val unitValue = jsonCurrency.getLong("unit_value")
                    val sellingRate = jsonCurrency.getString("selling_rate")
                    val currencyCode = jsonCurrency.getString("currency_code")
                    val buyingRate = jsonCurrency.getString("buying_rate")

                    val currencyObject = Currency(medianRate.toDouble(), unitValue, sellingRate.toDouble(), currencyCode, buyingRate.toDouble())

                    currencyList.add(currencyObject)
                    Log.d(TAG, "parseJsonData $currencyObject")

                }


            } catch (e: JSONException) {
                e.printStackTrace()
                Log.e(TAG, ".parseJsonData: Error processing Json data. ${e.message}")
            }
        }
        Log.d(TAG, "parseJsonData ends with $currencyList")
        return currencyList
    }

}