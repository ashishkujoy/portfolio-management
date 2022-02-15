package com.example.domain

import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class FundTest {

    @Test
    fun `add given stock`() {
        val lngStock = Stock("PETRONET LNG LIMITED")
        val adaniStock = Stock("ADANI ENTERPRISES LIMITED")
        val fund = Fund("ICICI_PRU_NIFTY_NEXT_50_INDEX", setOf(lngStock))

        fund.addStock(adaniStock)

        fund.shouldBeEqualToComparingFields(
            Fund(
                name = "ICICI_PRU_NIFTY_NEXT_50_INDEX",
                stocks = setOf(lngStock, adaniStock)
            ),
            ignorePrivateFields = false
        )
    }

    @Test
    fun `give zero percent overlap given funds does not have any common stock`() {
        val iciciFund = Fund(
            name = "ICICI_PRU_NIFTY_NEXT_50_INDEX",
            stocks = setOf(Stock("ADANI ENTERPRISES LIMITED"))
        )

        val paragParikhFund = Fund(
            name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
            stocks = setOf(Stock("3M INDIA LIMITED"))
        )

        iciciFund.overlappingStockPercentageWith(paragParikhFund) shouldBe BigDecimal("0.00")
    }

    @Test
    fun `give 100 percent overlap given funds having same stocks`() {
        val utiFund = Fund(
            name = "UTI_NIFTY_INDEX",
            stocks = setOf(Stock("ADANI ENTERPRISES LIMITED"))
        )

        val paragParikhFund = Fund(
            name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
            stocks = setOf(Stock("ADANI ENTERPRISES LIMITED"))
        )

        utiFund.overlappingStockPercentageWith(paragParikhFund) shouldBe BigDecimal("100.00")
    }

    @Test
    fun `give percentage of overlap given funds having some same stocks`() {
        val utiFund = Fund(
            name = "UTI_NIFTY_INDEX",
            stocks = setOf(
                Stock("ADANI ENTERPRISES LIMITED"),
                Stock("PI INDUSTRIES LIMITED"),
                Stock("THE INDIAN HOTELS COMPANY LIMITED"),
                Stock("CUMMINS INDIA LIMITED"),
                Stock("ABBOTT INDIA LIMITED"),
                Stock("KOTAK MAHINDRA BANK LIMITED")
            )
        )

        val paragParikhFund = Fund(
            name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
            stocks = setOf(
                Stock("ADANI ENTERPRISES LIMITED"),
                Stock("CUMMINS INDIA LIMITED"),
                Stock("DABUR INDIA LIMITED"),
            )
        )

        utiFund.overlappingStockPercentageWith(paragParikhFund) shouldBe BigDecimal("44.45")
    }
}