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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "CurrencyViewModel"

class CurrencyViewModel(private val currencyClient: CurrencyClient) : ViewModel()  {


    private val _currentResultMLD = MutableLiveData<Double>()
    val currentResultLD: LiveData<Double>
        get() = _currentResultMLD


    private val _currentCurrenciesMLD = MutableLiveData<ArrayList<Currency>>()
    val currentCurrenciesLD: LiveData<ArrayList<Currency>>
        get() = _currentCurrenciesMLD


    private val _currentSnackBarMLD = MutableLiveData<Boolean>()
    val currentSnackBarLD: LiveData<Boolean>
        get() = _currentSnackBarMLD


    init {
        val dateFormatted = getCurrentFormattedDate()
        getCurrency(dateFormatted)
    }

    fun getCurrency(date: String) {
        Log.d(TAG, "getCurrency: starts with $date")

        viewModelScope.launch {
            val result =  withContext(Dispatchers.IO) {currencyClient.requestResponse(date)}
            val status = currencyClient.getDownloadStatus()
            Log.d(TAG, "getCurrency:  response result is $result, and status $status")
            if(status == DownloadStatus.OK) {
                val arrayCurrency = convertJsonResponse(result)
                _currentCurrenciesMLD.value = arrayCurrency
            }
            else {
                // download failed
                _currentSnackBarMLD.value = true
                Log.d(TAG, "getCurrency failed with status $status. Error message is: $result")
            }
        }

        Log.d(TAG, "getCurrency: ends")

    }

    fun resetSnackBarState() {
        _currentSnackBarMLD.value = false
    }

    fun getCurrentFormattedDate () : String {
        //SimpleDateFormat is deprecated
        val dateNow =  GregorianCalendar(Locale.getDefault()).time
        Log.d(TAG, "getCurrentFormattedDate: dateNow is $dateNow")
        val dateFormatted = SimpleDateFormat("yyyy-MM-dd").format(dateNow) // it is deprecated but DateTimeFormatter also has warning
        Log.d(TAG, "getCurrentFormattedDate: formatted date  is $dateFormatted")
        return dateFormatted
    }

    fun calculateCurrency(convertFrom: String, convertTo: String, value:Double) {
        Log.d(TAG, "calculateCurrency: starts with $convertFrom, $convertTo, $value")

        if(convertFrom == convertTo) {
            _currentResultMLD.value = value
        }

        else {

            val currencyFrom = getCurrencyObject(convertFrom)
            val currencyTo = getCurrencyObject(convertTo)

            //calculation
            //HNB in Croatia doesn't allow directly converting from euro to usd for example, you need to convert it to HRK first
            val nationalValue = value * (currencyFrom?.buyingRate!! / currencyFrom.unitValue!!) // bank buying convertFrom value from you that is why buyingRate, dividing with unitValue to find value for 1
            val result = nationalValue / (currencyTo?.sellingRate!! / currencyTo.unitValue!!) // bank selling convertTo value to you that is why sellingRate
            _currentResultMLD.value = result

        }

        Log.d(TAG, "calculateCurrency: ends")
    }

    private fun getCurrencyObject(currencyCode: String): Currency? {
        for (currency in _currentCurrenciesMLD.value!!) {
            if (currency.currencyCode == currencyCode) {
                return currency
            }
        }
        return null
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