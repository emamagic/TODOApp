package com.codinginflow.mvvmtodo.safe

sealed class ErrorEntity(
        val error: String? = null,
        val code: Int? = null,
        val body: String? = null
) {

    class Network(message: String) : ErrorEntity(message)

    class Api(message: String? ,code: Int?= null ,body: String? = null) : ErrorEntity(message ,code ,body)

    class Unknown(message: String) : ErrorEntity(message)

}