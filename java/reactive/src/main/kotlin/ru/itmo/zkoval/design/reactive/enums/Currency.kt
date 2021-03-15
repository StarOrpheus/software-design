package ru.itmo.zkoval.design.reactive.enums

enum class Currency(private val coefToUsd: Double) {
    RUBLE(75.0), EURO(0.84), USD(1.0);

    fun convertToPreferred(price: Double, to: Currency): Double =
            price * this.coefToUsd / to.coefToUsd
}