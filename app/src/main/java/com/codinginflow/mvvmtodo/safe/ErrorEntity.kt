package com.codinginflow.mvvmtodo.safe

sealed class ErrorEntity(
        val message: String? = null,
        val code: Int? = null,
        val errorBody: String? = null
) {

    class Network(message: String) : ErrorEntity(message)

    class Api(message: String? ,code: Int?= null ,errorBody: String? = null) : ErrorEntity(message ,code ,errorBody)

    class Unknown(message: String) : ErrorEntity(message)

}