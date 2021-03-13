package com.example.currencyconverter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList
import com.example.currencyconverter.model.Currency


private const val TAG = "MainActivity"


class MainActivity : AppCompatActivity() {


    private val currencyViewModel : CurrencyViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))


        currencyViewModel.currentResultLD.observe(this, { result ->
            Log.d(TAG, "onCreate: observing user with $result")
            tvResult.text = getString(R.string.result_text, result.toString(), spinnerConvertTo.selectedItem.toString())
        })

        currencyViewModel.currentCurrenciesLD.observe(this, { result ->
            Log.d(TAG, "onCreate: observing user with $result")
            if(result != null) {
                setSpinners(result)
            }
        })

        currencyViewModel.toast.observe(this, { message ->
            message?.let {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                currencyViewModel.onToastShown()
            }
        })



        // so you can also click on arrow to open spinner
        ivSpinnerArrowConvertFrom.setOnClickListener { spinnerConvertFrom.performClick() }
        ivSpinnerArrowConvertTo.setOnClickListener { spinnerConvertTo.performClick() }

        btnCalculate.setOnClickListener {
            if(etValueFrom.text?.isEmpty()!! || etValueFrom.text?.length!! > 14) {
                etValueFrom.error = getString(R.string.error_valid_number)
            }
            else {
                currencyViewModel.calculateCurrency(spinnerConvertFrom.selectedItem.toString(), spinnerConvertTo.selectedItem.toString(), etValueFrom.text.toString().toDouble())
            }
        }

        Log.d(TAG, "onCreate: ends")
    }




    private fun setSpinners(fillArray: ArrayList<Currency>) {
        Log.d(TAG, "setSpinners: starts $fillArray")

        val codeList = ArrayList<String>()

        for (currency in fillArray) {
            codeList.add(currency.currencyCode!!)
        }

        val adapterConvertFrom = ArrayAdapter(this, R.layout.spinner_item, codeList)
        adapterConvertFrom.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerConvertFrom.adapter = adapterConvertFrom

        val adapterConvertTo = ArrayAdapter(this, R.layout.spinner_item, codeList)
        adapterConvertTo.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerConvertTo.adapter = adapterConvertTo
        Log.d(TAG, "setSpinners: ends with  $fillArray")
    }


}