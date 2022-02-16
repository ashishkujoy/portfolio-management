package com.example.cli

import com.example.domain.model.Fund
import com.example.domain.model.Portfolio
import com.example.domain.model.Stock
import com.example.domain.service.PortfolioService
import com.example.utils.doOnError
import com.example.utils.doOnSuccess
import com.example.utils.flatMap
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class CLIApplication(private val portfolioService: PortfolioService) {
    fun execute(commands: List<Command>): Result<List<String>> {
        return validateCommand(commands)
            .flatMap { validatedCommands ->
                val currentPortfolioCommand = validatedCommands.first() as CurrentPortfolioCommand
                portfolioService.newPortfolioWithFunds(currentPortfolioCommand.fundNames)
                    .map { portfolio -> portfolio to validatedCommands.drop(1) }
            }
            .map { (portfolio, validatedCommands) ->
                val output = mutableListOf<String>()
                validatedCommands.forEach { command ->
                    when (command) {
                        is CalculateOverlapCommand -> executeCalculateOverlapCommand(command, portfolio, output)
                        is AddStockCommand -> executeAddStockCommand(command, portfolio, output)
                        is AddFundCommand -> executeAddFundCommand(command, portfolio, output)
                        is CurrentPortfolioCommand -> throw Throwable("Only first command should be CURRENT_PORTFOLIO")
                    }
                }
                output.toList()
            }
    }

    private fun validateCommand(commands: List<Command>): Result<List<Command>> {
        if (commands.firstOrNull() !is CurrentPortfolioCommand) {
            return Result.failure(Throwable("First command should be CURRENT_PORTFOLIO"))
        }
        if (commands.filterIsInstance<CurrentPortfolioCommand>().size != 1) {
            return Result.failure(Throwable("Commands should have exactly one CURRENT_PORTFOLIO"))
        }
        return Result.success(commands)
    }

    private fun executeCalculateOverlapCommand(
        command: CalculateOverlapCommand,
        portfolio: Portfolio,
        output: MutableList<String>
    ) {
        portfolioService.calculateFundsOverlap(portfolio, command.fundName)
            .doOnSuccess { fundOverlaps ->
                fundOverlaps.forEach {
                    output.add("${it.fundName} ${it.overlapingFundName} ${it.overlapPercentage}%")
                }
            }
            .doOnError { output.add("FUND_NOT_FOUND") }
    }

    private fun executeAddStockCommand(
        command: AddStockCommand,
        portfolio: Portfolio,
        output: MutableList<String>
    ) {
        portfolioService.addStock(portfolio, command.fundName, command.stockName)
            .doOnError { output.add("FUND_NOT_FOUND") }
    }

    private fun executeAddFundCommand(
        command: AddFundCommand,
        portfolio: Portfolio,
        output: MutableList<String>
    ) {
        portfolioService.addFund(portfolio, command.fundName)
            .doOnError { output.add("FUND_NOT_FOUND") }
    }

    companion object {
        private val objectMapper = jacksonObjectMapper()
        fun new(): CLIApplication {
            val seedDataFile = File("./src/main/resources/seed-data.json")

            val funds = objectMapper
                .readValue<Set<Map<String, Any>>>(seedDataFile.readText())
                .map(::convertToFund)

            return CLIApplication(PortfolioService(funds))
        }

        private fun convertToFund(fundEntry: Map<String, Any>): Fund {
            val fundName = fundEntry["name"] as String
            val stocks = objectMapper
                .convertValue<List<String>>(fundEntry["stocks"] as Any)
                .map { stockName -> Stock(stockName) }

            return Fund(fundName, stocks)
        }
    }
}