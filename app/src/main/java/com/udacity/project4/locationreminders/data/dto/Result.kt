package com.udacity.project4.locationreminders.data.dto

sealed class Result<out T : Any> {

    data class Error(val message: String?, val code: Int? = null) : Result<Nothing>()

    data class Success<out T : Any>(val data: T) : Result<T>()
}