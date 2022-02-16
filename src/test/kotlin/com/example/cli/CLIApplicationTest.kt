package com.example.cli

import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.Test

class CLIApplicationTest {
    private val cliApplication = CLIApplication.new()

    @Test
    fun `execute given commands`() {
        cliApplication.execute(
            listOf(
                CurrentPortfolioCommand(setOf("ICICI_PRU_NIFTY_NEXT_50_INDEX", "MIRAE_ASSET_EMERGING_BLUECHIP")),
                CalculateOverlapCommand("UTI_NIFTY_INDEX")
            )
        ).shouldBeSuccess {
            it shouldBe listOf(
                "UTI_NIFTY_INDEX MIRAE_ASSET_EMERGING_BLUECHIP 65.51%",
                "UTI_NIFTY_INDEX ICICI_PRU_NIFTY_NEXT_50_INDEX 20.37%",
            )
        }
    }

    @Test
    fun `give error when first command is not current portfolio command`() {
        cliApplication.execute(
            listOf(
                CalculateOverlapCommand("UTI_NIFTY_INDEX"),
                CurrentPortfolioCommand(setOf("ICICI_PRU_NIFTY_NEXT_50_INDEX", "MIRAE_ASSET_EMERGING_BLUECHIP")),
            )
        ).shouldBeFailure {
            it shouldHaveMessage "First command should be CURRENT_PORTFOLIO"
        }
    }

    @Test
    fun `give error when there are multiple current portfolio command`() {
        cliApplication.execute(
            listOf(
                CurrentPortfolioCommand(setOf("ICICI_PRU_NIFTY_NEXT_50_INDEX", "MIRAE_ASSET_EMERGING_BLUECHIP")),
                CalculateOverlapCommand("UTI_NIFTY_INDEX"),
                CurrentPortfolioCommand(setOf("ICICI_PRU_NIFTY_NEXT_50_INDEX", "MIRAE_ASSET_EMERGING_BLUECHIP")),
            )
        ).shouldBeFailure {
            it shouldHaveMessage "Commands should have exactly one CURRENT_PORTFOLIO"
        }
    }

    @Test
    fun `give fund not found while adding stock to unknown fund`() {
        cliApplication.execute(
            listOf(
                CurrentPortfolioCommand(setOf("ICICI_PRU_NIFTY_NEXT_50_INDEX", "MIRAE_ASSET_EMERGING_BLUECHIP")),
                CalculateOverlapCommand("UTI_NIFTY_INDEX"),
                AddStockCommand("UN_KNOWN_FUND", "EPL LIMITED")
            )
        ).shouldBeSuccess {
            it shouldBe listOf(
                "UTI_NIFTY_INDEX MIRAE_ASSET_EMERGING_BLUECHIP 65.51%",
                "UTI_NIFTY_INDEX ICICI_PRU_NIFTY_NEXT_50_INDEX 20.37%",
                "FUND_NOT_FOUND"
            )
        }
    }

    @Test
    fun `give fund not found while add unknown fund in portfolio`() {
        cliApplication.execute(
            listOf(
                CurrentPortfolioCommand(setOf("ICICI_PRU_NIFTY_NEXT_50_INDEX", "MIRAE_ASSET_EMERGING_BLUECHIP")),
                CalculateOverlapCommand("UTI_NIFTY_INDEX"),
                AddFundCommand("UN_KNOWN_FUND")
            )
        ).shouldBeSuccess {
            it shouldBe listOf(
                "UTI_NIFTY_INDEX MIRAE_ASSET_EMERGING_BLUECHIP 65.51%",
                "UTI_NIFTY_INDEX ICICI_PRU_NIFTY_NEXT_50_INDEX 20.37%",
                "FUND_NOT_FOUND"
            )
        }
    }
}