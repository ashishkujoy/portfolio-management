package com.example.domain.model

import com.example.domain.error.FundNotFoundError

class Portfolio(funds: List<Fund>) {
    private val currentFunds = funds.toMutableList()

    fun addStockInFund(stockName: String, fundName: String): Result<Portfolio> {
        return findFund(fundName).map { fund ->
            fund.addStock(Stock(stockName))
            this
        }
    }

    fun calculateExistingFundsOverlapWith(fund: Fund): List<FundOverlap> {
        return currentFunds.map {
            val overlapPercentage = it.overlappingStockPercentageWith(fund)
            FundOverlap(fund.name, it.name, overlapPercentage)
        }
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