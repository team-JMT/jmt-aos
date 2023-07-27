package org.gdsc.presentation.utils


object ValidationUtils {
    fun checkName(inputText: String): Boolean {
        return nameRegex.containsMatchIn(inputText)
    }

    val nameRegex = Regex("[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\s]")
}
