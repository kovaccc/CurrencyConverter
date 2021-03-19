package com.example.currencyconverter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList
import com.example.currencyconverter.model.Currency
import com.google.android.material.snackbar.Snackbar


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val currencyViewModel : CurrencyViewModel by viewModel()

    private var snackbar:Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))


        currencyViewModel.currentCurrenciesLD.observe(this, { currencies ->
            Log.d(TAG, "onCreate: observing currencies with $currencies")
            setSpinners(currencies)
        })

        currencyViewModel.currentResultLD.observe(this, { result ->
            Log.d(TAG, "onCreate: observing result with $result")
            tvResult.text = result.toString()
        })

        currencyViewModel.currentSnackBarLD.observe(this, {
            if(it) {
                createSnackBar()
                snackbar!!.show()
            }
            else {
                if(snackbar != null) {
                    snackbar!!.dismiss()
                }
            }
        })

        // so you can also click on arrow to open spinner
        ivSpinnerArrowConvertFrom.setOnClickListener { spinnerConvertFrom.performClick() }
        ivSpinnerArrowConvertTo.setOnClickListener { spinnerConvertTo.performClick() }

        btnCalculate.setOnClickListener {
            if(etValueFrom.text?.isEmpty()!! || etValueFrom.text?.length!! > 14 || etValueFrom.text.toString() == ".") {
                etValueFrom.error = getString(R.string.error_valid_number)
            }
            else {
                currencyViewModel.calculateCurrency(spinnerConvertFrom.selectedItem.toString(), spinnerConvertTo.selectedItem.toString(), etValueFrom.text.toString().toDouble())
            }
        }

        Log.d(TAG, "onCreate: ends")
    }


    private fun createSnackBar() {
        snackbar =  Snackbar.make(findViewById(android.R.id.content), getString(R.string.snackbar_error_message), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.snackbar_try_again)) {
                        Log.d(TAG, "createSnackBar:  starts")
                        val dateFormatted = currencyViewModel.getCurrentFormattedDate()
                        Log.d(TAG, "formatted date is $dateFormatted")
                        currencyViewModel.getCurrency(dateFormatted)
                        currencyViewModel.resetSnackBarState()
                        Log.d(TAG, "createSnackBar: ends")
                }
    }


    private fun setSpinners(fillArray: ArrayList<Currency>) {
        Log.d(TAG, "setSpinners: starts $fillArray")

        val codeList = fillArray.map {currency -> // map will return mapped list of codes from fillArray
            currency.currencyCode
        }

        Log.d(TAG, "setSpinners: codeList is $codeList")

        val adapterConvertFrom = ArrayAdapter(this, R.layout.spinner_item, codeList)
        adapterConvertFrom.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerConvertFrom.adapter = adapterConvertFrom

        val adapterConvertTo = ArrayAdapter(this, R.layout.spinner_item, codeList)
        adapterConvertTo.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerConvertTo.adapter = adapterConvertTo

        Log.d(TAG, "setSpinners: ends with  $fillArray")
    }

}