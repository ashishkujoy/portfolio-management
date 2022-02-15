package com.example.domain.service

import com.example.domain.error.FundNotFoundError
import com.example.domain.model.Fund
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
}
