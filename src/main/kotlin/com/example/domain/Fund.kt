package com.example.domain

import java.math.BigDecimal
import java.math.RoundingMode

class Fund(private val name: String, stocks: Set<Stock>) {
    private val currentStocks = stocks.toMutableSet()

    fun addStock(stock: Stock): Fund {
        this.currentStocks.add(stock)
        return this
    }

    fun overlappingStockPercentageWith(otherFund: Fund): BigDecimal {
        val totalFunds = currentStocks.size + otherFund.currentStocks.size
        val commonFunds = currentStocks.intersect(otherFund.currentStocks).size

        return BigDecimal(((2.00 * commonFunds) / totalFunds) * 100.00).setScale(2, RoundingMode.CEILING)
    }
}
