package com.example.currencyconverter.module


import com.example.currencyconverter.APIService
import com.example.currencyconverter.CurrencyClient
import com.example.currencyconverter.CurrencyViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { CurrencyViewModel(get())}

}

val clientModule = module {
    single {CurrencyClient(get())} // singleton
}

val serviceModule = module {
    single { provideCurrencyAPIService()} // singleton API service
}


private fun provideCurrencyAPIService() : APIService = APIService.create()

