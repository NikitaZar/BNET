package ru.nikitazar.data.errors

import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class AppError(val code: Int, info: String) : RuntimeException(info) {
    companion object {
        fun from(e: Throwable): AppError = when (e) {
            is AppError -> e
            is UnknownHostException -> NetworkError
            is SocketTimeoutException -> NetworkError
            is IOException -> NetworkError
            else -> UnknownError
        }
    }
}

class ApiError(code: Int, message: String) : AppError(code, message)
object NetworkError : AppError(-1, "no_network")
object UnknownError : AppError(-2, "unknown")
