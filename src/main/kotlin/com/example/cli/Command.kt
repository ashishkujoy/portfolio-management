package com.example.cli

sealed class Command

data class CurrentPortfolioCommand(val fundNames: List<String>) : Command()
data class CalculateOverlapCommand(val fundName: String) : Command()
data class AddStockCommand(val fundName: String, val stockName: String) : Command()
data class AddFundCommand(val fundName: String) : Command()