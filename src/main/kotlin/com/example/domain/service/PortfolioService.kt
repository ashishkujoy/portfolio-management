package com.example.domain.service

import com.example.domain.error.FundNotFoundError
import com.example.domain.model.Fund
import com.example.domain.model.FundOverlap
import com.example.domain.model.Portfolio

class PortfolioService(private val masterFundsData: Set<Fund>) {
    fun newPortfolioWithFunds(fundNames: Set<String>): Result<Portfolio> {
        val nonExistingFundName = fundNames.find { fundName -> masterFundsData.none { it.name == fundName } }

        return if (nonExistingFundName != null) {
            Result.failure(FundNotFoundError(nonExistingFundName))
        } else {
            val funds = masterFundsData
                .filter { fund -> fundNames.contains(fund.name) }
                .map { fund -> fund.copy() }
                .toSet()

            Result.success(Portfolio(funds))
        }
    }

    fun addFund(portfolio: Portfolio, fundName: String): Result<Portfolio> {
        val fund = masterFundsData.find { it.name == fundName }

        return if (fund == null) {
            Result.failure(FundNotFoundError(fundName))
        } else {
            portfolio.addFund(fund.copy())
            Result.success(portfolio)
        }
    }

    fun addStock(portfolio: Portfolio, fundName: String, stockName: String): Result<Portfolio> {
        return portfolio.addStockInFund(stockName, fundName)
    }

    fun calculateFundsOverlap(portfolio: Portfolio, fundName: String): Result<List<FundOverlap>> {
        val fund = masterFundsData.find { it.name == fundName }

        return if(fund == null) {
            Result.failure(FundNotFoundError(fundName))
        } else {
            Result.success(portfolio.calculateExistingFundsOverlapWith(fund))
        }
    }
}
