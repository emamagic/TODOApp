package com.codinginflow.mvvmtodo.safe

interface ErrorHandler {
    fun getError(throwable: Throwable): ErrorEntity
}