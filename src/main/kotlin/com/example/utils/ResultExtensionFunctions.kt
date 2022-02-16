package com.example.utils

fun <T, O> Result<T>.flatMap(mapper: (T) -> Result<O>): Result<O> {
    return if (isFailure) {
        Result.failure(exceptionOrNull()!!)
    } else {
        mapper(getOrThrow())
    }
}

fun <T> Result<T>.doOnError(fn: (Throwable) -> Unit): Result<T> {
    if (isFailure) {
        fn(this.exceptionOrNull()!!)
    }
    return this
}

fun <T> Result<T>.doOnSuccess(fn: (T) -> Unit): Result<T> {
    if (isSuccess) {
        fn(getOrThrow())
    }
    return this
}