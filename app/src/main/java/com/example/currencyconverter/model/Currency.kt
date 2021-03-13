package com.example.currencyconverter.model

class Currency (var medianRate: Double? = 0.0, var unitValue:Long? = 0, var sellingRate: Double? = 0.0, var currencyCode: String? = "", var buyingRate:Double? = 0.0) {

    override fun toString(): String { // used for logs
        return "${this.medianRate}, ${this.unitValue}, ${this.sellingRate}, ${this.currencyCode}, ${this.buyingRate}"
    }
}