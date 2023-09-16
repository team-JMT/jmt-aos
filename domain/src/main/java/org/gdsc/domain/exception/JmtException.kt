package org.gdsc.domain.exception

import org.gdsc.domain.Empty

sealed class JmtException(
    message: String = String.Empty
) : Exception(message) {

    data class NetworkException(override val message: String = "네트워크 연결을 확인해주세요.") : JmtException()
    data class ServerException(override val message: String = "서버에 문제가 발생했습니다.") : JmtException()
    data class NoneDataException(override val message: String = "데이터가 없습니다.") : JmtException()
    data class UnKnownException(override val message: String = "알 수 없는 에러입니다.") : JmtException()
    data class GeneralException(override val message: String) : JmtException()

}