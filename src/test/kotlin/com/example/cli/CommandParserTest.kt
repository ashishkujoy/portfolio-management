package com.example.cli

import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CommandParserTest {

    @Test
    fun `successfully parse known commands`() {
        val result = CommandParser.parse("""
            CURRENT_PORTFOLIO AXIS_BLUECHIP ICICI_PRU_BLUECHIP UTI_NIFTY_INDEX
            CALCULATE_OVERLAP MIRAE_ASSET_EMERGING_BLUECHIP
            CALCULATE_OVERLAP MIRAE_ASSET_LARGE_CAP
            ADD_STOCK AXIS_BLUECHIP THE GREAT EASTERN SHIPPING COMPANY LIMITED
            NEW_FUND SBI_LARGE_&_MIDCAP
        """.trimIndent())

        result.shouldBeSuccess {
            it shouldBe listOf(
                CurrentPortfolioCommand(setOf("AXIS_BLUECHIP", "ICICI_PRU_BLUECHIP", "UTI_NIFTY_INDEX")),
                CalculateOverlapCommand("MIRAE_ASSET_EMERGING_BLUECHIP"),
                CalculateOverlapCommand("MIRAE_ASSET_LARGE_CAP"),
                AddStockCommand("AXIS_BLUECHIP", "THE GREAT EASTERN SHIPPING COMPANY LIMITED"),
                AddFundCommand("SBI_LARGE_&_MIDCAP"),
            )
        }
    }

    @Test
    fun `give failure for known command`() {
        val result = CommandParser.parse("""
            CURRENT_PORTFOLIO AXIS_BLUECHIP ICICI_PRU_BLUECHIP UTI_NIFTY_INDEX
            NEW_STOCK MIRAE_ASSET_EMERGING_BLUECHIP
            CALCULATE_OVERLAP MIRAE_ASSET_LARGE_CAP
        """.trimIndent())

        result.shouldBeFailure {
            it shouldBe CommandNotFound("NEW_STOCK MIRAE_ASSET_EMERGING_BLUECHIP")
        }
    }
}