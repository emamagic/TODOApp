package com.codinginflow.mvvmtodo.safe

import android.database.sqlite.SQLiteException
import com.codinginflow.mvvmtodo.safe.ErrorEntity
import com.codinginflow.mvvmtodo.safe.ErrorHandler
import java.io.IOException
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject

abstract class GeneralErrorHandlerImpl: ErrorHandler {

    override fun getError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is IOException,
            is UnknownHostException,
            is SocketException -> ErrorEntity.Network(message = "${throwable.message}//${throwable.cause}")
            is SQLiteException -> ErrorEntity.Api(message = "${throwable.message}//${throwable.cause}")
           // is HttpException -> ErrorEntity.Api(message = throwable.response()?.message() ,code = throwable.code() ,body = throwable.response()?.errorBody()?.string())
            else -> ErrorEntity.Unknown(message = "${throwable.message}//${throwable.cause}")
        }
    }

}