package com.example.domain.service

import com.example.domain.error.FundNotFoundError
import com.example.domain.model.Fund
import com.example.domain.model.FundOverlap
import com.example.domain.model.Portfolio

class PortfolioService(private val masterFundsData: List<Fund>) {
    fun newPortfolioWithFunds(fundNames: List<String>): Result<Portfolio> {
        val funds = fundNames.map(::findFund)
        val failure = funds.find { it.isFailure }

        return if (failure != null) {
            Result.failure(failure.exceptionOrNull()!!)
        } else {
            Result.success(Portfolio(funds.map { it.getOrThrow() }))
        }
    }

    fun addFund(portfolio: Portfolio, fundName: String): Result<Portfolio> {
        return findFund(fundName).map { fund ->
            portfolio.addFund(fund.copy())
        }
    }

    fun addStock(portfolio: Portfolio, fundName: String, stockName: String): Result<Portfolio> {
        return portfolio.addStockInFund(stockName, fundName)
    }

    fun calculateFundsOverlap(portfolio: Portfolio, fundName: String): Result<List<FundOverlap>> {
        return findFund(fundName).map { fund ->
            portfolio.calculateExistingFundsOverlapWith(fund)
        }
    }

    private fun findFund(fundName: String): Result<Fund> {
        val fund = masterFundsData.find { it.name == fundName }

        return if (fund == null) {
            Result.failure(FundNotFoundError(fundName))
        } else {
            Result.success(fund)
        }
    }
}
