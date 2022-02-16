package com.example.domain.service

import com.example.domain.error.FundNotFoundError
import com.example.domain.model.Fund
import com.example.domain.model.FundOverlap
import com.example.domain.model.Portfolio
import com.example.domain.model.Stock
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PortfolioServiceTest {
    private val iciciFund = Fund(
        name = "ICICI_PRU_NIFTY_NEXT_50_INDEX",
        stocks = listOf(
            Stock("INDRAPRASTHA GAS LIMITED"),
            Stock("COLGATE - PALMOLIVE (INDIA) LIMITED")
        )
    )
    private val pragFund = Fund(
        name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
        stocks = listOf(
            Stock("INDRAPRASTHA GAS LIMITED")
        )
    )
    private val utiFund = Fund(
        name = "UTI_NIFTY_INDEX",
        stocks = listOf(
            Stock("TVS MOTOR COMPANY LIMITED"),
            Stock("EQUITAS HOLDINGS LIMITED"),
            Stock("INDRAPRASTHA GAS LIMITED"),
        )
    )
    private val axisFund = Fund(
        name = "AXIS_BLUECHIP",
        stocks = listOf(Stock("INDRAPRASTHA GAS LIMITED"))
    )

    private val masterFundsData = listOf(iciciFund, pragFund, utiFund, axisFund)
    private val portfolioService = PortfolioService(masterFundsData)

    @Test
    fun `create portfolio having funds of given name`() {
        val result = portfolioService.newPortfolioWithFunds(
            fundNames = listOf(
                "ICICI_PRU_NIFTY_NEXT_50_INDEX",
                "UTI_NIFTY_INDEX"
            )
        )

        val expectedPortfolio = Portfolio(funds = listOf(iciciFund, utiFund))

        result.shouldBeSuccess {
            it shouldBe expectedPortfolio
        }
    }

    @Test
    fun `gives error if fund for given name is not present`() {
        val result = portfolioService.newPortfolioWithFunds(
            fundNames = listOf(
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
        val portfolio = Portfolio(funds = listOf(iciciFund))

        val result = portfolioService.addFund(portfolio, "UTI_NIFTY_INDEX")

        result.shouldBeSuccess {
            it shouldBe Portfolio(funds = listOf(iciciFund, utiFund))
        }
    }

    @Test
    fun `add stock in fund of given portfolio`() {
        val portfolio = Portfolio(funds = listOf(iciciFund))

        val result = portfolioService.addStock(
            portfolio,
            "ICICI_PRU_NIFTY_NEXT_50_INDEX",
            "TVS MOTOR COMPANY LIMITED"
        )

        result.shouldBeSuccess {
            it shouldBe Portfolio(funds = listOf(Fund(
                name = "ICICI_PRU_NIFTY_NEXT_50_INDEX",
                stocks = listOf(
                    Stock("INDRAPRASTHA GAS LIMITED"),
                    Stock("COLGATE - PALMOLIVE (INDIA) LIMITED"),
                    Stock("TVS MOTOR COMPANY LIMITED")
                )
            )))
        }
    }

    @Test
    fun `calculate fund overlap for given fund and portfolio`() {
        val portfolio = Portfolio(funds = listOf(iciciFund, utiFund, pragFund))

        val result = portfolioService.calculateFundsOverlap(portfolio, axisFund.name)

        result.shouldBeSuccess {
            it shouldBe listOf(
                FundOverlap(
                    fundName = "AXIS_BLUECHIP",
                    overlapingFundName = "ICICI_PRU_NIFTY_NEXT_50_INDEX",
                    overlapPercentage = BigDecimal("66.66")
                ),
                FundOverlap(
                    fundName = "AXIS_BLUECHIP",
                    overlapingFundName = "UTI_NIFTY_INDEX",
                    overlapPercentage = BigDecimal("50.00")
                ),
                FundOverlap(
                    fundName = "AXIS_BLUECHIP",
                    overlapingFundName = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
                    overlapPercentage = BigDecimal("100.00")
                ),

            )
        }

    }
}