package com.example.cli

data class CommandNotFound(val commandName: String): Throwable("Command not found $commandName")