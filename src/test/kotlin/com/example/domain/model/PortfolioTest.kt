package com.example.domain.model

import com.example.domain.error.FundNotFoundError
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeTypeOf
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PortfolioTest {

    @Test
    fun `portfolios having different funds should be different`() {
        val portfolio1 = Portfolio(funds = setOf(Fund("UTI_NIFTY_INDEX", emptySet())))
        val portfolio2 = Portfolio(funds = setOf(Fund("PARAG_PARIKH_CONSERVATIVE_HYBRID", emptySet())))

        portfolio1 shouldNotBe portfolio2
    }

    @Test
    fun `portfolios having same funds should be equal`() {
        val portfolio1 = Portfolio(funds = setOf(Fund("UTI_NIFTY_INDEX", emptySet())))
        val portfolio2 = Portfolio(funds = setOf(Fund("UTI_NIFTY_INDEX", emptySet())))

        portfolio1 shouldBe portfolio2
    }

    @Test
    fun `add stock successfully in portfolio`() {
        val portfolio = Portfolio(
            funds = setOf(
                Fund(
                    name = "UTI_NIFTY_INDEX",
                    stocks = setOf(Stock("INFOSYS LIMITED"))
                ),
                Fund(
                    name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
                    stocks = setOf(Stock("JK CEMENT LIMITED"))
                )
            )
        )

        portfolio.addStockInFund(stockName = "COFORGE LIMITED", fundName = "UTI_NIFTY_INDEX")

        val expectedPortfolio = Portfolio(
            funds = setOf(
                Fund(
                    name = "UTI_NIFTY_INDEX",
                    stocks = setOf(Stock("INFOSYS LIMITED"), Stock("COFORGE LIMITED"))
                ),
                Fund(
                    name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
                    stocks = setOf(Stock("JK CEMENT LIMITED"))
                )
            )
        )

        portfolio shouldBe expectedPortfolio
    }

    @Test
    fun `give error while add stock to fund which is not present in portfolio`() {
        val portfolio = Portfolio(
            funds = setOf(
                Fund(
                    name = "UTI_NIFTY_INDEX",
                    stocks = setOf(Stock("INFOSYS LIMITED"))
                )
            )
        )

        val result = portfolio.addStockInFund(
            stockName = "BHARTI AIRTEL LIMITED",
            fundName = "MIRAE_ASSET_EMERGING_BLUECHIP"
        )

        result.shouldBeFailure {
            it.shouldBeTypeOf<FundNotFoundError>()
            it.fundName shouldBe "MIRAE_ASSET_EMERGING_BLUECHIP"
        }
    }

    @Test
    fun `give overlap with funds present in portfolio`() {
        val portfolio = Portfolio(
            funds = setOf(
                Fund(
                    name = "UTI_NIFTY_INDEX",
                    stocks = setOf(
                        Stock("INFOSYS LIMITED"),
                        Stock("BATA INDIA LIMITED"),
                        Stock("PAGE INDUSTRIES LIMITED"),
                        Stock("TATA STEEL LIMITED"),
                    )
                ),
                Fund(
                    name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
                    stocks = setOf(
                        Stock("JK CEMENT LIMITED"),
                        Stock("TATA STEEL LIMITED"),
                    )
                )
            )
        )

        val actualFundsOverlapPercentage = portfolio.calculateExistingFundsOverlapWith(
            Fund(
                name = "AXIS_BLUECHIP",
                stocks = setOf(
                    Stock("TATA STEEL LIMITED"),
                )
            )
        )

        val expectedFundsOverlapPercentage = listOf(
            FundOverlap("AXIS_BLUECHIP", "UTI_NIFTY_INDEX", BigDecimal("40.00")),
            FundOverlap("AXIS_BLUECHIP", "PARAG_PARIKH_CONSERVATIVE_HYBRID",BigDecimal("66.66"))
        )

        actualFundsOverlapPercentage shouldBe expectedFundsOverlapPercentage
    }

    @Test
    fun `add fund in portfolio`() {
        val utiFund = Fund(
            name = "UTI_NIFTY_INDEX",
            stocks = setOf(Stock("INFOSYS LIMITED"))
        )
        val paragFund = Fund(
            name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
            stocks = setOf(Stock("JK CEMENT LIMITED"))
        )

        val portfolio = Portfolio(funds = setOf(utiFund))

        portfolio.addFund(paragFund)

        portfolio shouldBe Portfolio(setOf(utiFund, paragFund))
    }
}