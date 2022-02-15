package com.example.domain

import com.example.domain.error.FundNotFoundError

class Portfolio(private val funds: Set<Fund>) {
    fun addStockInFund(stockName: String, fundName: String): Result<Portfolio> {
        val fund = funds.find { it.name == fundName }

        return if (fund == null) {
            Result.failure(FundNotFoundError(fundName))
        } else {
            fund.addStock(Stock(stockName))
            Result.success(this)
        }
    }

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is Portfolio -> other.funds == funds
            else -> false
        }
    }

    override fun hashCode(): Int {
        return funds.hashCode()
    }

}