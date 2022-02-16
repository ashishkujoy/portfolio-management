package com.example.cli

object CommandParser {
    fun parse(command: String): Result<List<Command>> {
        return kotlin.runCatching {
            command
                .trim()
                .lines()
                .map { command ->
                    when {
                        command.startsWith("CURRENT_PORTFOLIO") -> parseCurrentPortfolioCommand(command)
                        command.startsWith("CALCULATE_OVERLAP") -> parseCalculateFundOverlapCommand(command)
                        command.startsWith("ADD_STOCK") -> parseAddStockCommand(command)
                        command.startsWith("NEW_FUND ") -> parseAddFundCommand(command)
                        else -> throw CommandNotFound(command)
                    }
                }
        }
    }

    private fun parseCurrentPortfolioCommand(rawCommand: String): Command {
        val fundNames = rawCommand.substringAfter("CURRENT_PORTFOLIO ").trim().split(" ")
        return CurrentPortfolioCommand(fundNames)
    }

    private fun parseCalculateFundOverlapCommand(rawCommand: String): Command {
        val fundName = rawCommand.substringAfter("CALCULATE_OVERLAP ").trim()
        return CalculateOverlapCommand(fundName)
    }

    private fun parseAddStockCommand(rawCommand: String): Command {
        val fundName = rawCommand.substringAfter("ADD_STOCK ").trim().substringBefore(" ")
        val stockName = rawCommand.substringAfter(fundName).trim()
        return AddStockCommand(fundName = fundName, stockName = stockName)
    }

    private fun parseAddFundCommand(rawCommand: String): Command {
        val fundName = rawCommand.substringAfter("NEW_FUND ").trim()
        return AddFundCommand(fundName)
    }
}