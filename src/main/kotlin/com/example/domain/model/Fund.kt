package com.example.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

class Fund(val name: String, stocks: List<Stock>) {
    private val currentStocks = stocks.toMutableList()

    fun addStock(stock: Stock): Fund {
        this.currentStocks.add(stock)
        return this
    }

    fun overlappingStockPercentageWith(otherFund: Fund): BigDecimal {
        val totalFunds = currentStocks.size + otherFund.currentStocks.size
        val commonFunds = currentStocks.intersect(otherFund.currentStocks.toSet()).size

        return BigDecimal(((2.00 * commonFunds) / totalFunds) * 100.00).setScale(2, RoundingMode.FLOOR)
    }

    fun copy(): Fund {
        return Fund(name, currentStocks.toList())
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Fund -> other.name == name && other.currentStocks == currentStocks
            else -> false
        }

    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + currentStocks.hashCode()
        return result
    }
}
