package com.example.domain.model

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class FundTest {

    @Test
    fun `funds having same name and current stocks should be same`() {
        val lngStock = Stock("PETRONET LNG LIMITED")
        val adaniStock = Stock("ADANI ENTERPRISES LIMITED")

        val iciciFund = Fund("ICICI_PRU_NIFTY_NEXT_50_INDEX", setOf(lngStock, adaniStock))
        val anotherIciciFund = Fund("ICICI_PRU_NIFTY_NEXT_50_INDEX", setOf(lngStock, adaniStock))

        iciciFund shouldBe anotherIciciFund
    }

    @Test
    fun `funds having different name but same current stocks should be different`() {
        val lngStock = Stock("PETRONET LNG LIMITED")
        val adaniStock = Stock("ADANI ENTERPRISES LIMITED")

        val iciciFund = Fund("ICICI_PRU_NIFTY_NEXT_50_INDEX", setOf(lngStock, adaniStock))
        val miraefund = Fund("MIRAE_ASSET_EMERGING_BLUECHIP", setOf(lngStock, adaniStock))

        iciciFund shouldNotBe miraefund
    }

    @Test
    fun `fund having same name but different current stocks should be different`() {
        val lngStock = Stock("PETRONET LNG LIMITED")
        val adaniStock = Stock("ADANI ENTERPRISES LIMITED")

        val iciciFund = Fund("ICICI_PRU_NIFTY_NEXT_50_INDEX", setOf(lngStock, adaniStock))
        val anotherIciciFund = Fund("ICICI_PRU_NIFTY_NEXT_50_INDEX", setOf(lngStock))

        iciciFund shouldNotBe anotherIciciFund
    }

    @Test
    fun `add given stock`() {
        val lngStock = Stock("PETRONET LNG LIMITED")
        val adaniStock = Stock("ADANI ENTERPRISES LIMITED")
        val fund = Fund("ICICI_PRU_NIFTY_NEXT_50_INDEX", setOf(lngStock))

        fund.addStock(adaniStock)

        fund shouldBe Fund(
            name = "ICICI_PRU_NIFTY_NEXT_50_INDEX",
            stocks = setOf(lngStock, adaniStock)
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

        utiFund.overlappingStockPercentageWith(paragParikhFund) shouldBe BigDecimal("44.44")
    }

    @Test
    fun `give a copy of fund`() {
        val fund = Fund(
            name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
            stocks = setOf(Stock("ADANI ENTERPRISES LIMITED"))
        )

        val copiedFund = fund.copy()

        copiedFund shouldBe fund
    }

    @Test
    fun `any update on copied fund should not be reflected on original fund`() {
        val originalFund = Fund(
            name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
            stocks = setOf(Stock("ADANI ENTERPRISES LIMITED"))
        )

        val copiedFund = originalFund.copy()
        copiedFund.addStock(Stock("ALPHABET INC."))


        assertSoftly {
            copiedFund shouldBe Fund(
                name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
                stocks = setOf(
                    Stock("ADANI ENTERPRISES LIMITED"),
                    Stock("ALPHABET INC."),
                )
            )

            originalFund shouldBe Fund(
                name = "PARAG_PARIKH_CONSERVATIVE_HYBRID",
                stocks = setOf(Stock("ADANI ENTERPRISES LIMITED"))
            )
        }
    }
}