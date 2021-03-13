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
        Log.d(TAG, "calculateCurrency: starts with $date")

        viewModelScope.launch {
            val result =  withContext(Dispatchers.IO) {currencyClient.requestResponse(date)}
            val status = currencyClient.getDownloadStatus()
            Log.d(TAG, "calculateCurrency:  response result is $result, and status $status")
            if(status == DownloadStatus.OK) {
                val arrayResults = convertJsonResponse(result)
                _currentCurrenciesMLD.value = arrayResults
            }
            else {
                // download failed
                Log.d(TAG, "runAzureML failed with status $status. Error message is: $result")
            }
        }

        Log.d(TAG, "calculateCurrency: ends")

    }


    fun calculateCurrency(convertFrom: String, convertTo: String, value:Double) {
        

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