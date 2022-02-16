package com.example

import com.example.cli.CLIApplication
import com.example.cli.CommandParser
import com.example.utils.doOnError
import com.example.utils.doOnSuccess
import com.example.utils.flatMap
import java.io.File

class PortfolioManagement {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            kotlin.runCatching {
                require(args.size == 1) {
                    "Input file name is required"
                }
            }.flatMap {
                kotlin.runCatching { File(args.first()).readText() }
            }.flatMap {
                CommandParser.parse(it)
            }.flatMap {
                CLIApplication.new().execute(it)
            }.doOnError {
                println("Error Occurred: ${it.message}")
            }.doOnSuccess { output ->
                output.forEach(::println)
            }
        }
    }
}