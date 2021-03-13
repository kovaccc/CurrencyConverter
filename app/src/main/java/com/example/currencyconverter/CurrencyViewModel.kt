package com.example.currencyconverter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.model.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val TAG = "CurrencyViewModel"

class CurrencyViewModel(private val currencyClient: CurrencyClient) : ViewModel()  {


    private val _currentResultMLD = MutableLiveData<Double>()
    val currentResultLD: LiveData<Double>
        get() = _currentResultMLD


    private val _currentCurrenciesMLD = MutableLiveData<ArrayList<Currency>>()
    val currentCurrenciesLD: LiveData<ArrayList<Currency>>
        get() = _currentCurrenciesMLD



    fun getCurrency(date: String) {
        Log.d(TAG, "getCurrency: starts with $date")

        viewModelScope.launch {
            val result =  withContext(Dispatchers.IO) {currencyClient.requestResponse(date)}
            val status = currencyClient.getDownloadStatus()
            Log.d(TAG, "getCurrency:  response result is $result, and status $status")
            if(status == DownloadStatus.OK) {
                val arrayResults = convertJsonResponse(result)
                _currentCurrenciesMLD.value = arrayResults
            }
            else {
                // download failed
                Log.d(TAG, "getCurrency failed with status $status. Error message is: $result")
            }
        }

        Log.d(TAG, "getCurrency: ends")

    }


    fun calculateCurrency(convertFrom: String, convertTo: String, value:Double) {
        var currencyFrom: Currency? = null
        var currencyTo: Currency? = null
        if(convertFrom == convertTo) {
            _currentResultMLD.value = value

        }
        else {
            for(currency in _currentCurrenciesMLD.value!!)  {
                if(currency.currencyCode == convertFrom) {
                    currencyFrom = currency
                }
                else if (currency.currencyCode == convertTo) {
                    currencyTo = currency
                }
            }

            //calculation
            //HNB in Croatia doesn't allow directly converting from euro to usd for example you need to convert it to HRK first
            val nationalValue = value * (currencyFrom?.buyingRate!! / currencyFrom.unitValue!!) // bank buying convertFrom value from you that is why buyingRate, dividing with unitValue to find value for 1
            val result = nationalValue / (currencyTo?.sellingRate!! / currencyTo.unitValue!!) // bank selling convertTO value from you that is why sellingRate
            _currentResultMLD.value = result

        }
    }


    private suspend fun convertJsonResponse(result: String) : ArrayList<Currency>{
        Log.d(TAG, "convertJsonResponse: starts with $result")
        val getResultJsonData = GetResultJsonData()
        val parsedData = getResultJsonData.parseJsonData(result)
        Log.d(TAG, "convertJsonResponse: ends with $parsedData")
        return parsedData
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared: starts")
        super.onCleared()
    }

}