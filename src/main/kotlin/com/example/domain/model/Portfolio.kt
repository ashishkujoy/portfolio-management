package com.example.domain.model

import com.example.domain.error.FundNotFoundError
import java.math.BigDecimal

class Portfolio(funds: Set<Fund>) {
    private val currentFunds = funds.toMutableSet()

    fun addStockInFund(stockName: String, fundName: String): Result<Portfolio> {
        return findFund(fundName).map { fund ->
            fund.addStock(Stock(stockName))
            this
        }
    }

    fun calculateExistingFundsOverlapWith(fund: Fund): List<BigDecimal> {
        return currentFunds.map { it.overlappingStockPercentageWith(fund) }
    }

    private fun findFund(fundName: String): Result<Fund> {
        val fund = currentFunds.find { it.name == fundName }
        return fund?.let { Result.success(it) } ?: Result.failure(FundNotFoundError(fundName))
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Portfolio -> other.currentFunds == currentFunds
            else -> false
        }
    }

    override fun hashCode(): Int {
        return currentFunds.hashCode()
    }

    fun addFund(fund: Fund): Portfolio {
        this.currentFunds.add(fund)
        return this
    }

}