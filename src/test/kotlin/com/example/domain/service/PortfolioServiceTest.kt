package com.example.domain.service

import com.example.domain.error.FundNotFoundError
import com.example.domain.model.Fund
import com.example.domain.model.Portfolio
import com.example.domain.model.Stock
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PortfolioServiceTest {
    private val iciciFund = Fund(
        name = "ICICI_PRU_NIFTY_NEXT_50_INDEX",
        stocks = setOf(
            Stock("INDRAPRASTHA GAS LIMITED"),
            Stock("COLGATE - PALMOLIVE (INDIA) LIMITED")
        )
    )
    private val pragFund = Fund(
        name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
        stocks = setOf(
            Stock("INDRAPRASTHA GAS LIMITED")
        )
    )
    private val utiFund = Fund(
        name = "UTI_NIFTY_INDEX",
        stocks = setOf(
            Stock("TVS MOTOR COMPANY LIMITED"),
            Stock("EQUITAS HOLDINGS LIMITED"),
            Stock("INDRAPRASTHA GAS LIMITED"),
        )
    )

    private val masterFundsData = setOf(iciciFund, pragFund, utiFund)
    private val portfolioService = PortfolioService(masterFundsData)

    @Test
    fun `create portfolio having funds of given name`() {
        val result = portfolioService.newPortfolioWithFunds(
            fundNames = setOf(
                "ICICI_PRU_NIFTY_NEXT_50_INDEX",
                "UTI_NIFTY_INDEX"
            )
        )

        val expectedPortfolio = Portfolio(funds = setOf(iciciFund, utiFund))

        result.shouldBeSuccess {
            it shouldBe expectedPortfolio
        }
    }

    @Test
    fun `gives error if fund for given name is not present`() {
        val result = portfolioService.newPortfolioWithFunds(
            fundNames = setOf(
                "ICICI_PRU_NIFTY_NEXT_50_INDEX",
                "UTI_NIFTY_INDEX",
                "MIRAE_ASSET_LARGE_CAP"
            )
        )

        result.shouldBeFailure {
            it shouldBe FundNotFoundError("MIRAE_ASSET_LARGE_CAP")
        }
    }

    @Test
    fun `add fund in given portfolio`() {
        val portfolio = Portfolio(funds = setOf(iciciFund))

        val result = portfolioService.addFund(portfolio, "UTI_NIFTY_INDEX")

        result.shouldBeSuccess {
            it shouldBe Portfolio(funds = setOf(iciciFund, utiFund))
        }
    }

    @Test
    fun `add stock in fund of given portfolio`() {
        val portfolio = Portfolio(funds = setOf(iciciFund))

        val result = portfolioService.addStock(
            portfolio,
            "ICICI_PRU_NIFTY_NEXT_50_INDEX",
            "TVS MOTOR COMPANY LIMITED"
        )

        result.shouldBeSuccess {
            it shouldBe Portfolio(funds = setOf(Fund(
                name = "ICICI_PRU_NIFTY_NEXT_50_INDEX",
                stocks = setOf(
                    Stock("INDRAPRASTHA GAS LIMITED"),
                    Stock("COLGATE - PALMOLIVE (INDIA) LIMITED"),
                    Stock("TVS MOTOR COMPANY LIMITED")
                )
            )))
        }
    }
}