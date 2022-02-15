package com.example.domain.error

data class FundNotFoundError(val fundName: String) : Throwable("Fund not found $fundName")