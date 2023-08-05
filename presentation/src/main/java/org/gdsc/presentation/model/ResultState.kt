package org.gdsc.presentation.model

sealed class ResultState<T> {
    companion object {
        fun <T> onFail(throwable: Throwable): ResultState<T> = OnFail(throwable)
        fun <T> onError(code: String = "", errorMsg: String = ""): ResultState<T> =
            OnRemoteError(code, errorMsg)

        fun <T> onSuccess(response: T): ResultState<T> =
            OnSuccess(response)

        fun <T> onLoading(): ResultState<T> = OnLoading()
    }

    data class OnSuccess<T>(var response: T) : ResultState<T>()
    data class OnRemoteError<T>(var code: String = "", var msg: String = "") : ResultState<T>()
    data class OnFail<T>(var throwable: Throwable) : ResultState<T>()
    data class OnLoading<T>(var blob: String = "") : ResultState<T>()

}

