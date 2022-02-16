package com.example

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.Charset

fun captureStandardOut(block: () -> Any?): String {
    val buffer = ByteArrayOutputStream()
    val previous = System.out
    System.setOut(PrintStream(buffer))

    try {
        block()
    } finally {
        System.setOut(previous)
    }

    return buffer.toString(Charset.defaultCharset())
}