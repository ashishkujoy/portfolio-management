package com.example

import com.example.cli.CLIApplication

class PortfolioManagement {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Portfolio management")
            args.forEach(::println)
            CLIApplication.new()
        }
    }
}