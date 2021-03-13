package com.example.currencyconverter

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.MalformedURLException


private const val TAG = "CurrencyClient"

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALISED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}

class CurrencyClient(private val currencyService: APIService) {

    private var downloadStatus = DownloadStatus.IDLE

    suspend fun requestResponse(date: String): String {

        Log.d(TAG, "requestResponse: starts with $date")


        try {
            downloadStatus = DownloadStatus.OK
            val response =
                currencyService.getCurrency(date)

            val responseString =  withContext(Dispatchers.IO) {
                response.body()?.string()
            }
            Log.d(TAG, "requestResponse: ends with $responseString")
            return responseString!!

        } catch (e: Exception) {
            val errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBackground: Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IO Exception reading data: ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "doInBackground: Security exception: Needs permission? ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "Unknown error: ${e.message}"
                }
            }
            Log.e(TAG, errorMessage)

            Log.d(TAG, "requestResponse: ends with $errorMessage")
            return errorMessage
        }
    }

    fun getDownloadStatus() : DownloadStatus {
        return downloadStatus
    }
}