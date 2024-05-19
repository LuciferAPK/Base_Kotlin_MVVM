package com.cyrus.base_kotlin_mvvm.base

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
open class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(var exceptionMessage: String? = null) : Result<Nothing>()
    data class InProgress(val inProgress: String = "Loading") : Result<Nothing>()
    data class Failure(val returnCode: Int, val failureMessage: String? = null) : Result<Nothing>()
    data class Failures<out T : Any>(
        val data: T? = null,
        val returnCode: Int,
        val failureMessage: String? = null
    ) : Result<Nothing>()

    final override fun toString(): String {
        return when (this) {
            is Success<*> -> "$data"
            is Error -> "$exceptionMessage"
            is InProgress -> inProgress
            is Failure -> "$failureMessage"
            is Failures<*> -> "$returnCode: $data, $failureMessage"
            else -> ""
        }
    }
}
