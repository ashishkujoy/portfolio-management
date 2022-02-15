package com.example.domain.model

import com.example.domain.error.FundNotFoundError
import java.math.BigDecimal

class Portfolio(private val funds: Set<Fund>) {
    fun addStockInFund(stockName: String, fundName: String): Result<Portfolio> {
        return findFund(fundName).map { fund ->
            fund.addStock(Stock(stockName))
            this
        }
    }

    fun calculateExistingFundsOverlapWith(fund: Fund): List<BigDecimal> {
        return funds.map { it.overlappingStockPercentageWith(fund) }
    }

    private fun findFund(fundName: String): Result<Fund> {
        val fund = funds.find { it.name == fundName }
        return fund?.let { Result.success(it) } ?: Result.failure(FundNotFoundError(fundName))
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Portfolio -> other.funds == funds
            else -> false
        }
    }

    override fun hashCode(): Int {
        return funds.hashCode()
    }

}