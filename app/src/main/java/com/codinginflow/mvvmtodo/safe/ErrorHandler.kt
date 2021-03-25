package com.codinginflow.mvvmtodo.safe

import com.codinginflow.mvvmtodo.safe.ErrorEntity


interface ErrorHandler {
    fun getError(throwable: Throwable): ErrorEntity
}