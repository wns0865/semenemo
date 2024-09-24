package com.semonemo.presentation.screen.signup

import com.semonemo.presentation.util.Validator
import java.io.File

data class SignUpUiState(
    val nickname: String = "",
    val password: String = "",
    val profileImage: File? = null,
) {
    fun validate(): Boolean =
        profileImage != null &&
            nickname.isNotBlank() &&
            password.isNotBlank() &&
            Validator.validationNickname(nickname).isEmpty() &&
            Validator
                .validationPassword(
                    password,
                ).isEmpty()
}
