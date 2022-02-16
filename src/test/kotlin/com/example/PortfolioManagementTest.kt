package com.example

import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class PortfolioManagementTest {
    @Test
    fun `run application for given input command file`() {
        val actualOutput = captureStandardOut {
            PortfolioManagement.main(arrayOf("./src/test/resources/input.txt"))
        }
        val expectedOutput = """ICICI_PRU_NIFTY_NEXT_50_INDEX UTI_NIFTY_INDEX 20.37%
ICICI_PRU_NIFTY_NEXT_50_INDEX AXIS_MIDCAP 14.81%
ICICI_PRU_NIFTY_NEXT_50_INDEX PARAG_PARIKH_FLEXI_CAP 7.40%
FUND_NOT_FOUND
ICICI_PRU_NIFTY_NEXT_50_INDEX UTI_NIFTY_INDEX 20.37%
ICICI_PRU_NIFTY_NEXT_50_INDEX AXIS_MIDCAP 14.67%
ICICI_PRU_NIFTY_NEXT_50_INDEX PARAG_PARIKH_FLEXI_CAP 7.31%"""

        actualOutput shouldContain expectedOutput
    }

    @Test
    fun `give error for unknown file input`() {
        val actualOutput = captureStandardOut {
            PortfolioManagement.main(arrayOf("./src/test/resources/bad.txt"))
        }

        actualOutput shouldContain "Error Occurred: ./src/test/resources/bad.txt (No such file or directory)"
    }

    @Test
    fun `give error when there is no input to main`() {
        val actualOutput = captureStandardOut {
            PortfolioManagement.main(arrayOf())
        }

        actualOutput shouldContain "Input file name is required"
    }
}