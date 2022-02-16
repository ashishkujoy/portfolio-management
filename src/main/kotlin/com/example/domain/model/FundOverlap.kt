package com.example.domain.model

import java.math.BigDecimal

data class FundOverlap(
    val fundName: String,
    val overlapingFundName: String,
    val overlapPercentage: BigDecimal,
)
